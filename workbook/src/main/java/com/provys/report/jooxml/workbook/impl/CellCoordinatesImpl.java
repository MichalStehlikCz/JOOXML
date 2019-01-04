package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellReference;

import java.util.Objects;

public class CellCoordinatesImpl implements CellCoordinates {

    private static final Logger LOG = LogManager.getLogger(CellCoordinatesImpl.class.getName());
    private final int row;
    private final int col;

    static CellCoordinatesImpl parse(String address) {
        Objects.requireNonNull(address);
        CellReference cellReference = new CellReference(address);
        if (cellReference.getSheetName() != null) {
            LOG.error("Sheet is not supportd in CellCoordinates - parse of {} failed", address);
            throw new IllegalArgumentException("Sheet is not supportd in CellCoordinates - parse failed " + address);
        }
        if (cellReference.isRowAbsolute()) {
            LOG.error("Absolute row reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute row reference not supported in CellAddress - " + address);
        }
        if (cellReference.isColAbsolute()) {
            LOG.error("Absolute column reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute column reference not supported in CellAddress - " + address);
        }
        return new CellCoordinatesImpl(cellReference.getRow(), cellReference.getCol());
    }

    CellCoordinatesImpl(int row, int col) {
        if (row < 0) {
            LOG.error("Row index must be positive, not {}", row);
            throw new IllegalArgumentException("Row index must be positive");
        }
        if (col < 0) {
            LOG.error("Column index must be positive, not {}", col);
            throw new IllegalArgumentException("Column index must be positive");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public CellCoordinates shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellCoordinatesImpl(getRow() + rowShift, getCol() + colShift);
    }

    @Override
    public CellCoordinates shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellCoordinatesImpl that = (CellCoordinatesImpl) o;
        return getRow() == that.getRow() &&
                getCol() == that.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    @Override
    public String toString() {
        return "CellCoordinatesImpl{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
