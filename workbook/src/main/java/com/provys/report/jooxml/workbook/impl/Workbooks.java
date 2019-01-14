package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Workbooks {

    public static @Nonnull CellCoordinates getCellCoordinates(int row, int col) {
        return CellCoordinatesImpl.of(row, col);
    }

    public static @Nonnull CellCoordinates parseCellCoordinates(String address) {
        return CellCoordinatesImpl.parse(address);
    }

    public static @Nonnull CellReference getCellReference(@Nullable String sheetName, int row, int col,
                                                          boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceImpl.of(sheetName, row, col, rowAbsolute, colAbsolute);
    }

    public static @Nonnull CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceImpl.of(row, col, rowAbsolute, colAbsolute);
    }

    public static @Nonnull CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates,
                                                          boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceImpl.of(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    public static @Nonnull CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute,
                                                          boolean colAbsolute) {
        return CellReferenceImpl.of(coordinates, rowAbsolute, colAbsolute);
    }

    public static @Nonnull CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return CellReferenceImpl.of(sheetName, row, col);
    }

    public static @Nonnull CellReference getCellReference(int row, int col) {
        return CellReferenceImpl.of(row, col);
    }

    public static @Nonnull CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellReferenceImpl.of(sheetName, coordinates);
    }

    public static @Nonnull CellReference getCellReference(CellCoordinates coordinates) {
        return CellReferenceImpl.of(coordinates);
    }

    public static CellReference parseCellReference(String address) {
        return CellReferenceImpl.parse(address);
    }

    public static CellValue getFormulaValue(String formula) {
        return new CellValueFormula(formula);
    }

    public static CellValue getStringValue(@Nullable String value) {
        return CellValueString.of(value);
    }

    public static CellValue getNumericValue(@Nullable Double value) {
        return CellValueNumeric.of(value);
    }

    public static CellValue getBooleanValue(@Nullable Boolean value) {
        return CellValueBoolean.of(value);
    }

    public static CellValue getErrorValue(@Nullable Byte value) {
        return CellValueError.of(value);
    }

    public static CellValue getBlankValue() {
        return CellValueBlank.get();
    }

    public static CellProperties getProperties(@Nullable Integer styleIndex) {
        return new CellPropertiesImpl(styleIndex);
    }

}
