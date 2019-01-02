package com.provys.report.jooxml.workbook;

import org.jetbrains.annotations.Nullable;

public interface CellValueFactory {
    CellValue getFormulaValue(String formula);
    CellValue getStringValue(@Nullable String value);
    CellValue getNumericValue(@Nullable Double value);
    CellValue getBooleanValue(@Nullable Boolean value);
    CellValue getErrorValue(@Nullable Byte value);
}
