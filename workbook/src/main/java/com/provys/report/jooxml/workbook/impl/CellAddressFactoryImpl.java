package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"unused"})
public class CellAddressFactoryImpl implements CellAddressFactory {

    @Override
    @Nonnull
    public CellAddressInt of(@Nullable String sheetName, int row, int col) {
        return CellAddressInt.of(sheetName, row, col);
    }

    @Override
    @Nonnull
    public CellAddressInt of(int row, int col) {
        return CellAddressInt.of(row, col);
    }

    @Override
    @Nonnull
    public CellAddressInt of(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellAddressInt.of(sheetName, coordinates);
    }

    @Override
    @Nonnull
    public CellAddressInt of(CellCoordinates coordinates) {
        return CellAddressInt.of(coordinates);
    }

    @Override
    @Nonnull
    public CellAddressInt parse(String address) {
        return CellAddressInt.parse(address);
    }

}
