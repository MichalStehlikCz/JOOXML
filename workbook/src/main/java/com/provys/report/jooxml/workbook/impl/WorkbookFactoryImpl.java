package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class WorkbookFactoryImpl implements WorkbookProvider {

    @Override
    public CellCoordinates getCellCoordinates(int row, int col) {
        return Workbooks.getCellCoordinates(row, col);
    }

    @Override
    public CellCoordinates parseCellCoordinates(String address) {
        return Workbooks.parseCellCoordinates(address);
    }

    @Override
    @Nonnull
    public CellAddress getCellAddress(@Nullable String sheetName, int row, int col) {
        return CellAddress.of(sheetName, row, col);
    }

    @Override
    @Nonnull
    public CellAddress getCellAddress(int row, int col) {
        return CellAddress.of(row, col);
    }

    @Override
    @Nonnull
    public CellAddress getCellAddress(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellAddress.of(sheetName, coordinates);
    }

    @Override
    @Nonnull
    public CellAddress getCellAddress(CellCoordinates coordinates) {
        return CellAddress.of(coordinates);
    }

    @Override
    @Nonnull
    public CellAddress parseCellAddress(String address) {
        return CellAddress.parse(address);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute,
                                          boolean colAbsolute) {
        return Workbooks.getCellReference(sheetName, row, col, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return Workbooks.getCellReference(row, col, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return Workbooks.getCellReference(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return Workbooks.getCellReference(coordinates, rowAbsolute, colAbsolute);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return Workbooks.getCellReference(sheetName, row, col);
    }

    @Override
    public CellReference getCellReference(int row, int col) {
        return Workbooks.getCellReference(row, col);
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return Workbooks.getCellReference(sheetName, coordinates);
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates) {
        return Workbooks.getCellReference(coordinates);
    }

    @Override
    public CellReference parseCellReference(String address) {
        return Workbooks.parseCellReference(address);
    }

    @Override
    public CellValue getFormulaValue(String formula) {
        return Workbooks.getFormulaValue(formula);
    }

    @Override
    public CellValue getStringValue(@Nullable String value) {
        return Workbooks.getStringValue(value);
    }

    @Override
    public CellValue getNumericValue(@Nullable Double value) {
        return Workbooks.getNumericValue(value);
    }

    @Override
    public CellValue getBooleanValue(@Nullable Boolean value) {
        return Workbooks.getBooleanValue(value);
    }

    @Override
    public CellValue getErrorValue(@Nullable Byte value) {
        return Workbooks.getErrorValue(value);
    }

    @Override
    public CellValue getBlankValue() {
        return Workbooks.getBlankValue();
    }

    @Override
    public CellProperties getProperties(@Nullable Integer styleIndex) {
        return Workbooks.getProperties(styleIndex);
    }

}