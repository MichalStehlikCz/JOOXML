package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

class CellValueBlank implements CellValue {

    private static final CellValueBlank instance = new CellValueBlank();

    static CellValueBlank get() {
        return instance;
    }

    private CellValueBlank() {}

    @Override
    public CellType getCellType() {
        return CellType.BLANK;
    }
}
