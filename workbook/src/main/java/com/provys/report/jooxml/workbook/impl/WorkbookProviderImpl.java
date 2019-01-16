package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WorkbookProviderImpl implements WorkbookProvider {

    @Override
    public CellCoordinates getCellCoordinates(int row, int col) {
        return CellCoordinatesInt.of(row, col);
    }

    @Override
    public CellCoordinates parseCellCoordinates(String address) {
        return CellCoordinatesInt.parse(address);
    }

    @Override
    @Nonnull
    public CellAddressInt getCellAddress(@Nullable String sheetName, int row, int col) {
        return CellAddressInt.of(sheetName, row, col);
    }

    @Override
    @Nonnull
    public CellAddressInt getCellAddress(int row, int col) {
        return CellAddressInt.of(row, col);
    }

    @Override
    @Nonnull
    public CellAddressInt getCellAddress(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellAddressInt.of(sheetName, coordinates);
    }

    @Override
    @Nonnull
    public CellAddressInt getCellAddress(CellCoordinates coordinates) {
        return CellAddressInt.of(coordinates);
    }

    @Override
    @Nonnull
    public CellAddressInt parseCellAddress(String address) {
        return CellAddressInt.parse(address);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute,
                                          boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, row, col, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(row, col, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return CellReferenceInt.of(sheetName, row, col);
    }

    @Override
    public CellReference getCellReference(int row, int col) {
        return CellReferenceInt.of(row, col);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellReferenceInt.of(sheetName, coordinates);
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates) {
        return CellReferenceInt.of(coordinates);
    }

    @Override
    public CellReference parseCellReference(String address) {
        return CellReferenceInt.parse(address);
    }

    @Override
    public CellValue getFormulaValue(String formula) {
        return CellValueFormula.of(formula);
    }

    @Override
    public CellValue getStringValue(@Nullable String value) {
        return CellValueString.of(value);
    }

    @Override
    public CellValue getNumericValue(@Nullable Double value) {
        return CellValueNumeric.of(value);
    }

    @Override
    public CellValue getBooleanValue(@Nullable Boolean value) {
        return CellValueBoolean.of(value);
    }

    @Override
    public CellValue getErrorValue(@Nullable Byte value) {
        return CellValueError.of(value);
    }

    @Override
    public CellValue getBlankValue() {
        return CellValueBlank.get();
    }

}
