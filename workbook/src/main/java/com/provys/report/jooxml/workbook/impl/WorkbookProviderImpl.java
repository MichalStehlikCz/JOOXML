package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WorkbookProviderImpl implements WorkbookProvider {

    @Override
    @Nonnull
    public CellCoordinates getCellCoordinates(int row, int col) {
        return CellCoordinatesInt.of(row, col);
    }

    @Override
    @Nonnull
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
    @Nonnull
    public CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute,
                                          boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, row, col, rowAbsolute, colAbsolute);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(row, col, rowAbsolute, colAbsolute);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return CellReferenceInt.of(sheetName, row, col);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(int row, int col) {
        return CellReferenceInt.of(row, col);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellReferenceInt.of(sheetName, coordinates);
    }

    @Override
    @Nonnull
    public CellReference getCellReference(CellCoordinates coordinates) {
        return CellReferenceInt.of(coordinates);
    }

    @Override
    @Nonnull
    public CellReference parseCellReference(String address) {
        return CellReferenceInt.parse(address);
    }

    @Override
    @Nonnull
    public RowProperties getRowProperties(float heightInPoints, boolean hidden, int styleIndex) {
        return RowPropertiesInt.of(heightInPoints, hidden, styleIndex);
    }

    @Override
    @Nonnull
    public CellValue getFormulaValue(String formula) {
        return CellValueFormula.of(formula);
    }

    @Override
    @Nonnull
    public CellValue getStringValue(@Nullable String value) {
        return CellValueString.of(value);
    }

    @Override
    @Nonnull
    public CellValue getNumericValue(@Nullable Double value) {
        return CellValueNumeric.of(value);
    }

    @Override
    @Nonnull
    public CellValue getBooleanValue(@Nullable Boolean value) {
        return CellValueBoolean.of(value);
    }

    @Override
    @Nonnull
    public CellValue getErrorValue(@Nullable Byte value) {
        return CellValueError.of(value);
    }

    @Override
    @Nonnull
    public CellValue getBlankValue() {
        return CellValueBlank.get();
    }

}
