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

package com.provys.jooxml.worksheet;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;

/**
 * SXSSF wrapper for a cell under evaluation
 */
final class RXSSFEvaluationCell implements EvaluationCell {
    private final EvaluationSheet _evalSheet;
    private final RXSSFCell _cell;

    public RXSSFEvaluationCell(RXSSFCell cell, RXSSFEvaluationSheet evaluationSheet) {
        _cell = cell;
        _evalSheet = evaluationSheet;
    }

    public RXSSFEvaluationCell(RXSSFCell cell) {
        this(cell, new RXSSFEvaluationSheet(cell.getSheet()));
    }

    @Override
    public Object getIdentityKey() {
        // save memory by just using the cell itself as the identity key
        // Note - this assumes RXSSFCell has not overridden hashCode and equals
        return _cell;
    }

    public RXSSFCell getSXSSFCell() {
        return _cell;
    }
    @Override
    public boolean getBooleanCellValue() {
        return _cell.getBooleanCellValue();
    }
    /**
     * @return cell type
     */
    @Override
    public CellType getCellType() {
        return _cell.getCellType();
    }
    /**
     * @since POI 3.15 beta 3
     * @deprecated use <code>getCellType</code> instead
     * Will be deleted when we make the CellType enum transition. See bug 59791.
     */
    @Deprecated
    @Removal(version = "4.2")
    @Internal(since="POI 3.15 beta 3")
    @Override
    public CellType getCellTypeEnum() {
        return _cell.getCellTypeEnum();
    }
    @Override
    public int getColumnIndex() {
        return _cell.getColumnIndex();
    }
    @Override
    public int getErrorCellValue() {
        return _cell.getErrorCellValue();
    }
    @Override
    public double getNumericCellValue() {
        return _cell.getNumericCellValue();
    }
    @Override
    public int getRowIndex() {
        return _cell.getRowIndex();
    }
    @Override
    public EvaluationSheet getSheet() {
        return _evalSheet;
    }
    @Override
    public String getStringCellValue() {
        return _cell.getRichStringCellValue().getString();
    }
    
    @Override
	public CellRangeAddress getArrayFormulaRange() {
		return _cell.getArrayFormulaRange();
	}
	
	@Override
	public boolean isPartOfArrayFormulaGroup() {
		return _cell.isPartOfArrayFormulaGroup();
	}
	
    /**
     * @return cell type of cached formula result
     */
    @Override
    public CellType getCachedFormulaResultType() {
        return _cell.getCachedFormulaResultType();
    }
    /**
     * @since POI 3.15 beta 3
     * @deprecated POI 3.15 beta 3.
     * Will be deleted when we make the CellType enum transition. See bug 59791.
     */
    @Deprecated
    @Removal(version = "4.2")
    @Internal(since="POI 3.15 beta 3")
    @Override
    public CellType getCachedFormulaResultTypeEnum() {
        return getCachedFormulaResultType();
    }
}
