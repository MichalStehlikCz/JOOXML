package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellReference;

import java.util.Objects;
import java.util.Optional;

public class CellCoordinatesImpl implements CellCoordinates {

    private static final Logger LOG = LogManager.getLogger(CellCoordinatesImpl.class.getName());
    private static CellCoordinatesImpl A1 = new CellCoordinatesImpl(0, 0);
    private final int row;
    private final int col;

    /**
     * Parse supplied address and convert it to cell coordinates
     *
     * @param address is cell address (as in Excel - e.g. A1)
     * @return cell coordinates cooresponding to supplied address
     * @throws IllegalArgumentException if supplied address is not valid Excel cell reference, it contains sheet
     * reference or it is has absolute reference to row or column
     */
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
        return of(cellReference.getRow(), cellReference.getCol());
    }

    /**
     * Get cell coordinates with given address. Might cache instances, thus is used instead of constructor
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     * @return instance of CellCoordinates with given coordinates
     * @throws IllegalArgumentException if either coordinate is negative
     */
    static CellCoordinatesImpl of(int row, int col) {
        if ((row == 0) && (col == 0)) {
            return A1;
        }
        return new CellCoordinatesImpl(row, col);
    }

    /**
     * Convert column index (zero based) to excel string representing the column (e.g. A, B, ...)
     *
     * @param builder is string builder result should be appended to
     * @param col is column index (zero based)
     * @throws IllegalArgumentException if supplied column index is negative
     */
    static void appendColAsString(StringBuilder builder, int col) {
        if (col < 0) {
            throw new IllegalArgumentException("Cannot convert negative number to column string");
        }
        if (col > 25) {
            appendColAsString(builder, (col / 26) - 1);
        }
        builder.append((char) ((col % 26) + 65)); // 64 is 1 for zero > one based + 64 code of A
    }

    private CellCoordinatesImpl(int row, int col) {
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

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public void appendAddress(StringBuilder builder) {
        appendColAsString(builder, getCol());
        builder.append(getRow() + 1);
    }

    @Override
    public String getAddress() {
        StringBuilder builder = new StringBuilder();
        appendAddress(builder);
        return builder.toString();
    }

    @Override
    public Optional<CellCoordinates> shiftByOrEmpty(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return Optional.of(this);
        } else if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(of(getRow() + rowShift, getCol() + colShift));
    }

    @Override
    public CellCoordinates shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return of(getRow() + rowShift, getCol() + colShift);
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
