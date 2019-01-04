package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;
import org.jetbrains.annotations.Nullable;

public class WorkbookFactoryImpl implements WorkbookFactoryInt {
    @Override
    public CellCoordinates getCellCoordinates(int row, int col) {
        return null;
    }

    @Override
    public CellCoordinates parseCellCoordinates(String formula) {
        return null;
    }

    @Override
    public CellAddress getCellAddress(@Nullable String sheetName, int row, int col) {
        return null;
    }

    @Override
    public CellAddress getCellAddress(int row, int col) {
        return null;
    }

    @Override
    public CellAddress getCellAddress(@Nullable String sheetName, CellCoordinates coordinates) {
        return null;
    }

    @Override
    public CellAddress getCellAddress(CellCoordinates coordinates) {
        return null;
    }

    @Override
    public CellAddress parseCellAddress(String formula) {
        return null;
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return null;
    }

    @Override
    public CellReference getCellReference(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return null;
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return null;
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return null;
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, int row, int col) {
        return null;
    }

    @Override
    public CellReference getCellReference(int row, int col) {
        return null;
    }

    @Override
    public CellReference getCellReference(@Nullable String sheetName, CellCoordinates coordinates) {
        return null;
    }

    @Override
    public CellReference getCellReference(CellCoordinates coordinates) {
        return null;
    }

    @Override
    public CellReference parseCellReference(String formula) {
        return null;
    }

    @Override
    public CellValue getFormulaValue(String formula) {
        return new CellValueFormula(formula);
    }

    @Override
    public CellValue getStringValue(@Nullable String value) {
        return null;
    }

    @Override
    public CellValue getNumericValue(@Nullable Double value) {
        return null;
    }

    @Override
    public CellValue getBooleanValue(@Nullable Boolean value) {
        return null;
    }

    @Override
    public CellValue getErrorValue(@Nullable Byte value) {
        return null;
    }

    @Override
    public CellValue getBlankValue() {
        return null;
    }

    @Override
    public CellProperties getProperties(@Nullable Integer styleIndex, @Nullable Comment comment) {
        return new CellPropertiesImpl(styleIndex, comment);
    }

    @Override
    public Comment getComment(boolean visible, @Nullable String author, @Nullable String text) {
        return null;
    }

}
