package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CellValueString extends CellValueBase<String> {

    /**
     * Creates new cell value of string type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return string cell value with given value
     */
    @Nonnull
    public static CellValueString of(@Nullable String value) {
        return new CellValueString(value);
    }

    private CellValueString(@Nullable String value) {
        super(value);
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.STRING;
    }

    /**
     * Override toString as we want to put value in ""
     *
     * @return string representation of object in form{@literal CellValueString(value="<value>")}
     */
    @Override
    @Nonnull
    public String toString() {
        return "CellValueString{" +
                "value=" + getValue().map(val -> '\'' + val + '\'').orElse("null") +
                '}';
    }
}
