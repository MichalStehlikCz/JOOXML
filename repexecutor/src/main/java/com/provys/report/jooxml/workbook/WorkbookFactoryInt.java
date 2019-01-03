package com.provys.report.jooxml.workbook;

import org.jetbrains.annotations.Nullable;

public interface WorkbookFactoryInt {
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
    CellValue getFormulaValue(String formula);
    CellValue getStringValue(@Nullable String value);
    CellValue getNumericValue(@Nullable Double value);
    CellValue getBooleanValue(@Nullable Boolean value);
    CellValue getErrorValue(@Nullable Byte value);
    CellValue getBlankValue();
    CellProperties getProperties(@Nullable Integer styleIndex, @Nullable Comment comment);
    Comment getComment(boolean visible, @Nullable String author, @Nullable String text);
}
