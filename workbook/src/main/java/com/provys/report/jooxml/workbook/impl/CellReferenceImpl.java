package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellReference;

public class CellReferenceImpl extends CellAddressImpl implements CellReference {

    private final boolean rowAbsolute;
    private final boolean colAbsolute;

    public CellReferenceImpl(String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        super(sheetName, row, col);
        this.rowAbsolute = rowAbsolute;
        this.colAbsolute = colAbsolute;
    }

    @Override
    public boolean isRowAbsolute() {
        return rowAbsolute;
    }

    @Override
    public boolean isColAbsolute() {
        return colAbsolute;
    }

    @Override
    public CellReferenceImpl shiftBy(int rowShift, int colShift) {
        return new CellReferenceImpl(getSheetName().orElse(null), getRow() + rowShift, getCol() + colShift,
                isRowAbsolute(), isColAbsolute());
    }
}
