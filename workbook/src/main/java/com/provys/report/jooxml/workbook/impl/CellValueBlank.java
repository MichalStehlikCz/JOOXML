package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;

public class CellValueBlank implements CellValueInt {

    @Nonnull
    private static final CellValueBlank instance = new CellValueBlank();

    /**
     * @return instance of blank cell value
     */
    @Nonnull
    public static CellValueBlank get() {
        return instance;
    }

    private CellValueBlank() {}

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.BLANK;
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellValueBlank{}";
    }
}
