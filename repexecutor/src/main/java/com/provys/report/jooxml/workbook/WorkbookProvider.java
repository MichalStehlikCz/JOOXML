package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface allows to inject workbook provider to report executor
 */
public interface WorkbookProvider {
    CellCoordinates getCellCoordinates(int row, int col);
    CellCoordinates parseCellCoordinates(String formula);
    CellAddress getCellAddress(@Nullable String sheetName, int row, int col);
    CellAddress getCellAddress(int row, int col);
    CellAddress getCellAddress(@Nullable String sheetName, CellCoordinates coordinates);
    CellAddress getCellAddress(CellCoordinates coordinates);
    CellAddress parseCellAddress(String formula);
    CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute);
    CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute);
    CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute);
    CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute);
    CellReference getCellReference(@Nullable String sheetName, int row, int col);
    CellReference getCellReference(int row, int col);
    CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates);
    CellReference getCellReference(CellCoordinates coordinates);
    CellReference parseCellReference(String formula);

    /**
     * Return instance of RowProperties with given characteristics
     *
     * @param heightInPoints is height in points for given row, null if left on default height
     * @param hidden indicates if row should be hidden
     * @param styleIndex is style index (within given workbook), null if no style is specified on row level
     * @return instance of RowProperties with given characteristics
     */
    @Nonnull
    RowProperties getRowProperties(@Nullable Float heightInPoints, boolean hidden, @Nullable Short styleIndex);

    CellValue getFormulaValue(String formula);
    CellValue getStringValue(@Nullable String value);
    CellValue getNumericValue(@Nullable Double value);
    CellValue getBooleanValue(@Nullable Boolean value);
    CellValue getErrorValue(@Nullable Byte value);
    CellValue getBlankValue();
}
