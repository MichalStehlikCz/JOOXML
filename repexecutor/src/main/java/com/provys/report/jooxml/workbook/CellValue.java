package com.provys.report.jooxml.workbook;

import javax.annotation.Nullable;

public interface CellValue {

    static CellValue ofFormula(String formula) {
        return Workbooks.getFormulaValue(formula);
    }

    static CellValue ofString(@Nullable String value) {
        return Workbooks.getStringValue(value);
    }

    static CellValue ofNumeric(@Nullable Double value) {
        return Workbooks.getNumericValue(value);
    }

    static CellValue ofBoolean(@Nullable Boolean value) {
        return Workbooks.getBooleanValue(value);
    }

    static CellValue ofError(@Nullable Byte value) {
        return Workbooks.getErrorValue(value);
    }

    static CellValue ofBlank() {
        return Workbooks.getBlankValue();
    }

    CellType getCellType();

}
