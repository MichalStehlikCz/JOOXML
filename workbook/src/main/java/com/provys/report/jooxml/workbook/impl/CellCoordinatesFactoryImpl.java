package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellCoordinatesFactory;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public final class CellCoordinatesFactoryImpl implements CellCoordinatesFactory {

    public CellCoordinatesFactoryImpl() {}

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
