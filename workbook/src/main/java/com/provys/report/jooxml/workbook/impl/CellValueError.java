package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CellValueError extends CellValueBase<Byte> {

    /**
     * Creates new cell value of error type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return error cell value with given value
     */
    @Nonnull
    public static CellValueError of(@Nullable Byte value) {
        return new CellValueError(value);
    }

    private CellValueError(@Nullable Byte value) {
        super(value);
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.ERROR;
    }
}
