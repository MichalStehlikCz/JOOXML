package com.provys.report.jooxml.workbook;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Workbooks {

    private static Workbooks instance;

    /**
     * Create cell coordinates for specified row and column.
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     * @return coordinates based on specified indices
     */
    public static CellCoordinates getCellCoordinates(int row, int col) {
        return instance.getWorkbookFactory().getCellCoordinates(row, col);
    }

    public static CellCoordinates parseCellCoordinates(String address) {
        return instance.getWorkbookFactory().parseCellCoordinates(address);
    }

    public static CellValue getFormulaValue(String formula) {
        return instance.getWorkbookFactory().getFormulaValue(formula);
    }

    public static CellValue getStringValue(@Nullable String value) {
        return instance.getWorkbookFactory().getStringValue(value);
    }

    public static CellValue getNumericValue(@Nullable Double value) {
        return instance.getWorkbookFactory().getNumericValue(value);
    }

    public static CellValue getBooleanValue(@Nullable Boolean value) {
        return instance.getWorkbookFactory().getBooleanValue(value);
    }

    public static CellValue getErrorValue(@Nullable Byte value) {
        return instance.getWorkbookFactory().getErrorValue(value);
    }

    public static CellValue getBlankValue() {
        return instance.getWorkbookFactory().getBlankValue();
    }

    public static CellProperties getProperties(@Nullable Integer styleIndex) {
        return instance.getWorkbookFactory().getProperties(styleIndex);
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private WorkbookFactoryInt workbookFactory;

    /**
     * Initializes static pointer to Workbooks instance. Invoked from WorkbooksSEInitializer or WorkbooksEEInitializer.
     */
    void init() {
        instance = this;
    }

    private WorkbookFactoryInt getWorkbookFactory() {
        return workbookFactory;
    }
}
