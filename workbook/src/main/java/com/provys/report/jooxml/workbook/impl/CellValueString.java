package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import javax.annotation.Nullable;

public class CellValueString extends CellValueBase<String> {

    static CellValueString of(@Nullable String value) {
        return new CellValueString(value);
    }

    private CellValueString(@Nullable String value) {
        super(value);
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    /**
     * Override toString as we want to put value in ""
     *
     * @return string representation of object in form{@literal CellValueString(value="<value>")}
     */
    @Override
    public String toString() {
        return "CellValueString{" +
                "value=" + getValue().map(val -> "\"" + val + "\"").orElse("null") +
                '}';
    }
}
