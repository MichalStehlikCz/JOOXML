package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellCoordinatesFactory;

import javax.annotation.Nonnull;

public class CellCoordinatesFactoryImpl implements CellCoordinatesFactory {

    private static final CellCoordinatesFactoryImpl INSTANCE = new CellCoordinatesFactoryImpl();

    @Nonnull
    public static CellCoordinatesFactoryImpl getInstance() {
        return INSTANCE;
    }

    @Nonnull
    @Override
    public CellCoordinates of(int row, int col) {
        return CellCoordinatesInt.of(row, col);
    }

    @Nonnull
    @Override
    public CellCoordinates parse(String address) {
        return CellCoordinatesInt.parse(address);
    }
}
