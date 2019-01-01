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
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.util.Internal;

/**
 * SXSSF wrapper for a sheet under evaluation
 */
@Internal
final class RXSSFEvaluationSheet implements EvaluationSheet {
    private final RXSSFSheet _xs;
    private int _lastDefinedRow = -1;

    public RXSSFEvaluationSheet(RXSSFSheet sheet) {
        _xs = sheet;
        _lastDefinedRow = _xs.getLastRowNum();
    }

    public RXSSFSheet getSXSSFSheet() {
        return _xs;
    }

    /* (non-Javadoc)
     * @see org.apache.poi.ss.formula.EvaluationSheet#getlastRowNum()
     * @since POI 4.0.0
     */
    @Override
    public int getLastRowNum() {
        return _lastDefinedRow;
    }
    
    @Override
    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        RXSSFRow row = _xs.getRow(rowIndex);
        if (row == null) {
            if (rowIndex <= _xs.getLastFlushedRowNum()) {
                throw new RXSSFFormulaEvaluator.RowFlushedException(rowIndex);
            }
            return null;
        }
        RXSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return new RXSSFEvaluationCell(cell, this);
    }
    
    /* (non-JavaDoc), inherit JavaDoc from EvaluationSheet
     * @since POI 3.15 beta 3
     */
    @Override
    public void clearAllCachedResultValues() {
        _lastDefinedRow = _xs.getLastRowNum();
    }
}
