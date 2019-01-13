package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;
import org.jetbrains.annotations.Nullable;

public class Workbooks {

    public static CellCoordinates getCellCoordinates(int row, int col) {
        return CellCoordinatesImpl.of(row, col);
    }

    public static CellCoordinates parseCellCoordinates(String address) {
        return CellCoordinatesImpl.parse(address);
    }

    public static CellAddress getCellAddress(@Nullable String sheetName, int row, int col) {
        return getCellAddress(sheetName, getCellCoordinates(row, col));
    }

    public static CellAddress getCellAddress(int row, int col) {
        return getCellAddress(null, row, col);
    }

    public static CellAddress getCellAddress(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellAddressImpl.of(sheetName, coordinates);
    }

    public static CellAddress getCellAddress(CellCoordinates coordinates) {
        return getCellAddress(null, coordinates);
    }

    public static CellAddress parseCellAddress(String address) {
        return CellAddressImpl.parse(address);
    }

    public static CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute,
                                          boolean colAbsolute) {
        return getCellReference(sheetName, getCellCoordinates(row, col), rowAbsolute, colAbsolute);
    }

    public static CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return getCellReference(null, row, col, rowAbsolute, colAbsolute);
    }

    public static CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceImpl.of(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    public static CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceImpl.of(coordinates, rowAbsolute, colAbsolute);
    }

    public static CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return getCellReference(sheetName, row, col, false, false);
    }

    public static CellReference getCellReference(int row, int col) {
        return getCellReference(null, row, col);
    }

    public static CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellReferenceImpl.of(sheetName, coordinates);
    }

    public static CellReference getCellReference(CellCoordinates coordinates) {
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
