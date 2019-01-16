package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CellValueNumeric extends CellValueBase<Double> {

    @Nonnull
    static CellValueNumeric of(@Nullable Double value) {
        return new CellValueNumeric(value);
    }

    private CellValueNumeric(@Nullable Double value) {
        super(value);
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

}
