package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.jetbrains.annotations.Nullable;

public class CellValueBoolean extends CellValueBase<Boolean> {

    static CellValueBoolean of(@Nullable Boolean value) {
      return new CellValueBoolean(value);
    }

    private CellValueBoolean(@Nullable Boolean value) {
        super(value);
    }

    @Override
    public CellType getCellType() {
        return CellType.BOOLEAN;
    }

}
