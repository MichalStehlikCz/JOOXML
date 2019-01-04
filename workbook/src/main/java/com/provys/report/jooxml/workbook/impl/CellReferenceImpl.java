package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

class CellReferenceImpl extends CellAddressImpl implements CellReference {

    private static final Logger LOG = LogManager.getLogger(CellReferenceImpl.class.getName());
    private final boolean rowAbsolute;
    private final boolean colAbsolute;

    static CellReferenceImpl parse(String address) {
        Objects.requireNonNull(address);
        org.apache.poi.ss.util.CellReference cellReference = new org.apache.poi.ss.util.CellReference(address);
        if (cellReference.isRowAbsolute()) {
            LOG.error("Absolute row reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute row reference not supported in CellAddress - " + address);
        }
        if (cellReference.isColAbsolute()) {
            LOG.error("Absolute column reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute column reference not supported in CellAddress - " + address);
        }
        return new CellReferenceImpl(cellReference.getSheetName(),
                Workbooks.getCellCoordinates(cellReference.getRow(), cellReference.getCol()),
                cellReference.isRowAbsolute(), cellReference.isColAbsolute());
    }


    CellReferenceImpl(String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        super(sheetName, coordinates);
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
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellReferenceImpl(getSheetName().orElse(null),
                Workbooks.getCellCoordinates(getRow() + rowShift, getCol() + colShift),
                isRowAbsolute(), isColAbsolute());
    }

    @Override
    public CellReferenceImpl shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }
}
