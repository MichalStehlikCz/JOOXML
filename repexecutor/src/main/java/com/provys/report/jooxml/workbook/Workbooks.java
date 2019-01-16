package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

class Workbooks {

    private static WorkbookProvider provider;

    @Nonnull
    static CellCoordinates getCellCoordinates(int row, int col) {
        return provider.getCellCoordinates(row, col);
    }

    @Nonnull
    static CellCoordinates parseCellCoordinates(String address) {
        return provider.parseCellCoordinates(address);
    }

    @Nonnull
    static CellValue getFormulaValue(String formula) {
        return provider.getFormulaValue(formula);
    }

    @Nonnull
    static CellValue getStringValue(@Nullable String value) {
        return provider.getStringValue(value);
    }

    @Nonnull
    static CellValue getNumericValue(@Nullable Double value) {
        return provider.getNumericValue(value);
    }

    @Nonnull
    static CellValue getBooleanValue(@Nullable Boolean value) {
        return provider.getBooleanValue(value);
    }

    @Nonnull
    static CellValue getErrorValue(@Nullable Byte value) {
        return provider.getErrorValue(value);
    }

    @Nonnull
    static CellValue getBlankValue() {
        return provider.getBlankValue();
    }

    static void setWorkbookProvider(WorkbookProvider provider) {
        Workbooks.provider = Objects.requireNonNull(provider);
    }

    /**
     * Not instantiable class, it acts as holder for granting access to workbook provider
     */
    private Workbooks() {}
}
