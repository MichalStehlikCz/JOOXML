package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.jetbrains.annotations.Nullable;

public class CellValueNumeric extends CellValueBase<Double> {

    static CellValueNumeric of(@Nullable Double value) {
        return new CellValueNumeric(value);
    }

    private CellValueNumeric(@Nullable Double value) {
        super(value);
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

}
