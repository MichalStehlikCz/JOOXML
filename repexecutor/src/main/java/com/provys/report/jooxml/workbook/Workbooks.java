package com.provys.report.jooxml.workbook;

import javax.annotation.Nullable;
import java.util.Objects;

class Workbooks {

    private static WorkbookProvider provider;

    static CellCoordinates getCellCoordinates(int row, int col) {
        return provider.getCellCoordinates(row, col);
    }

    static CellCoordinates parseCellCoordinates(String address) {
        return provider.parseCellCoordinates(address);
    }

    static CellValue getFormulaValue(String formula) {
        return provider.getFormulaValue(formula);
    }

    static CellValue getStringValue(@Nullable String value) {
        return provider.getStringValue(value);
    }

    static CellValue getNumericValue(@Nullable Double value) {
        return provider.getNumericValue(value);
    }

    static CellValue getBooleanValue(@Nullable Boolean value) {
        return provider.getBooleanValue(value);
    }

    static CellValue getErrorValue(@Nullable Byte value) {
        return provider.getErrorValue(value);
    }

    static CellValue getBlankValue() {
        return provider.getBlankValue();
    }

    static CellProperties getProperties(@Nullable Integer styleIndex) {
        return provider.getProperties(styleIndex);
    }

    static void setWorkbookProvider(WorkbookProvider provider) {
        Workbooks.provider = Objects.requireNonNull(provider);
    }

    /**
     * Not instantiable class, it acts as holder for granting access to workbook provider
     */
    private Workbooks() {};
}
