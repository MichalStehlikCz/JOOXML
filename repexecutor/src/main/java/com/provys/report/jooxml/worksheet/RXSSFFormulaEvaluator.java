/* ====================================================================
   Copyright 2018 Michal Stehlik

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   Derived from work that is part of Apache POI project, licenced by
   the Apache Software Foundation (ASF) under Apache Licnece, Version 2.0
==================================================================== */

package com.provys.report.jooxml.worksheet;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.IStabilityClassifier;
import org.apache.poi.ss.formula.WorkbookEvaluator;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.BaseXSSFFormulaEvaluator;

/**
 * Streaming-specific Formula Evaluator, which is able to 
 *  lookup cells within the current Window.
 */
public final class RXSSFFormulaEvaluator extends BaseXSSFFormulaEvaluator {
    private static final POILogger logger = POILogFactory.getLogger(RXSSFFormulaEvaluator.class);
    
    private RXSSFWorkbook wb;
    
    public RXSSFFormulaEvaluator(RXSSFWorkbook workbook) {
        this(workbook, null, null);
    }
    private RXSSFFormulaEvaluator(RXSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this(workbook, new WorkbookEvaluator(RXSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder));
    }
    private RXSSFFormulaEvaluator(RXSSFWorkbook workbook, WorkbookEvaluator bookEvaluator) {
        super(bookEvaluator);
        this.wb = workbook;
    }
    
    /**
     * @param stabilityClassifier used to optimise caching performance. Pass <code>null</code>
     * for the (conservative) assumption that any cell may have its definition changed after
     * evaluation begins.
     * @param udfFinder pass <code>null</code> for default (AnalysisToolPak only)
     */
    public static RXSSFFormulaEvaluator create(RXSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        return new RXSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
    }
    public void notifySetFormula(Cell cell) {
        _bookEvaluator.notifyUpdateCell(new RXSSFEvaluationCell((RXSSFCell)cell));
    }
    public void notifyDeleteCell(Cell cell) {
        _bookEvaluator.notifyDeleteCell(new RXSSFEvaluationCell((RXSSFCell)cell));
    }
    public void notifyUpdateCell(Cell cell) {
        _bookEvaluator.notifyUpdateCell(new RXSSFEvaluationCell((RXSSFCell)cell));
    }


    /**
     * Turns a RXSSFCell into a RXSSFEvaluationCell
     */
    @Override
    protected EvaluationCell toEvaluationCell(Cell cell) {
        if (!(cell instanceof RXSSFCell)){
            throw new IllegalArgumentException("Unexpected type of cell: " + cell.getClass() + "." +
                    " Only SXSSFCells can be evaluated.");
        }

        return new RXSSFEvaluationCell((RXSSFCell)cell);
    }
    
    @Override
    public RXSSFCell evaluateInCell(Cell cell) {
        return (RXSSFCell) super.evaluateInCell(cell);
    }
    
    /**
     * For active worksheets only, will loop over rows and
     *  cells, evaluating formula cells there.
     * If formula cells are outside the window for that sheet,
     *  it can either skip them silently, or give an exception
     */
    public static void evaluateAllFormulaCells(RXSSFWorkbook wb, boolean skipOutOfWindow) {
        RXSSFFormulaEvaluator eval = new RXSSFFormulaEvaluator(wb);
        
        // Check they're all available
        for (Sheet sheet : wb) {
            if (((RXSSFSheet)sheet).areAllRowsFlushed()) {
                throw new SheetsFlushedException();
            }
        }
        
        // Process the sheets as best we can
        for (Sheet sheet : wb) {
            
            // Check if any rows have already been flushed out
            int lastFlushedRowNum = ((RXSSFSheet) sheet).getLastFlushedRowNum();
            if (lastFlushedRowNum > -1) {
                if (! skipOutOfWindow) throw new RowFlushedException(0);
                logger.log(POILogger.INFO, "Rows up to " + lastFlushedRowNum + " have already been flushed, skipping");
            }
            
            // Evaluate what we have
            for (Row r : sheet) {
                for (Cell c : r) {
                    if (c.getCellType() == CellType.FORMULA) {
                        eval.evaluateFormulaCell(c);
                    }
                }
            }
        }
    }
    
    /**
     * Loops over rows and cells, evaluating formula cells there.
     * If any sheets are inactive, or any cells outside of the window,
     *  will give an Exception.
     * For SXSSF, you generally don't want to use this method, instead
     *  evaluate your formulas as you go before they leave the window.
     */
    public void evaluateAll() {
        // Have the evaluation done, with exceptions
        evaluateAllFormulaCells(wb, false);
    }
    
    public static class SheetsFlushedException extends IllegalStateException {
        protected SheetsFlushedException() {
            super("One or more sheets have been flushed, cannot evaluate all cells");
        }
    }
    public static class RowFlushedException extends IllegalStateException {
        protected RowFlushedException(int rowNum) {
            super("RowImpl " + rowNum + " has been flushed, cannot evaluate all cells");
        }
    }
}
