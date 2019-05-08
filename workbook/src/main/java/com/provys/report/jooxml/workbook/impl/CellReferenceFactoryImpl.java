package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellReferenceFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
class CellReferenceFactoryImpl implements CellReferenceFactory {

    @Nonnull
    @Override
    public CellReference of(@Nullable String sheetName, int row, int col, boolean rowAbsolute,
                                          boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, row, col, rowAbsolute, colAbsolute);
    }

    @Nonnull
    @Override
    public CellReference of(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(row, col, rowAbsolute, colAbsolute);
    }

    @Nonnull
    @Override
    public CellReference of(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    @Nonnull
    @Override
    public CellReference of(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return CellReferenceInt.of(coordinates, rowAbsolute, colAbsolute);
    }

    @Nonnull
    @Override
    public CellReference of(@Nullable String sheetName, int row, int col) {
        return CellReferenceInt.of(sheetName, row, col);
    }

    @Nonnull
    @Override
    public CellReference of(int row, int col) {
        return CellReferenceInt.of(row, col);
    }

    @Nonnull
    @Override
    public CellReference of(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellReferenceInt.of(sheetName, coordinates);
    }

    @Nonnull
    @Override
    public CellReference of(CellCoordinates coordinates) {
        return CellReferenceInt.of(coordinates);
    }

    @Nonnull
    @Override
    public String getRegex() {
        return CellReferenceInt.REGEX;
    }

    @Nonnull
    @Override
    public CellReference parse(String address) {
        return CellReferenceInt.parse(address);
    }

}
