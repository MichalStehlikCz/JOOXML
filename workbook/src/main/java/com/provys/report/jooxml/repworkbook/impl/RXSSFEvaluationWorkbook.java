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

package com.provys.report.jooxml.repworkbook.impl;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.xssf.usermodel.BaseXSSFEvaluationWorkbook;
import org.apache.poi.util.Internal;

/**
 * SXSSF wrapper around the SXSSF and XSSF workbooks
 */
@Internal
public final class RXSSFEvaluationWorkbook extends BaseXSSFEvaluationWorkbook {
    private final RXSSFWorkbook _uBook;
    
    public static RXSSFEvaluationWorkbook create(RXSSFWorkbook book) {
        if (book == null) {
            return null;
        }
        return new RXSSFEvaluationWorkbook(book);
    }
    
    private RXSSFEvaluationWorkbook(RXSSFWorkbook book) {
        super(book.getXSSFWorkbook());
        _uBook = book;
    }

    @Override
    public int getSheetIndex(EvaluationSheet evalSheet) {
        RXSSFSheet sheet = ((RXSSFEvaluationSheet)evalSheet).getSXSSFSheet();
        return _uBook.getSheetIndex(sheet);
    }

    @Override
    public EvaluationSheet getSheet(int sheetIndex) {
        return new RXSSFEvaluationSheet(_uBook.getSheetAt(sheetIndex));
    }

    @Override
    public Ptg[] getFormulaTokens(EvaluationCell evalCell) {
        RXSSFCell cell = ((RXSSFEvaluationCell)evalCell).getSXSSFCell();
        return FormulaParser.parse(cell.getCellFormula(), this, FormulaType.CELL, _uBook.getSheetIndex(cell.getSheet()));
    }
}
