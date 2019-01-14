package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import javax.annotation.Nullable;

public class CellValueError extends CellValueBase<Byte> {

    static CellValueError of(@Nullable Byte value) {
        return new CellValueError(value);
    }

    private CellValueError(@Nullable Byte value) {
        super(value);
    }

    @Override
    public CellType getCellType() {
        return CellType.ERROR;
    }
}
