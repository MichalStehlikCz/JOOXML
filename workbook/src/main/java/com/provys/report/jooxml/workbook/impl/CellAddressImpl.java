package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellReference;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CellAddressImpl implements CellAddress {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private final String sheetName;
    private final CellCoordinates coordinates;

    public static CellAddressImpl parse(String address) {
        Objects.requireNonNull(address);
        CellReference cellReference = new CellReference(address);
        if (cellReference.isRowAbsolute()) {
            LOG.error("Absolute row reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute row reference not supported in CellAddress - " + address);
        }
        if (cellReference.isColAbsolute()) {
            LOG.error("Absolute column reference not supported in CellAddress {}", address);
            throw new IllegalArgumentException("Absolute column reference not supported in CellAddress - " + address);
        }
        return new CellAddressImpl(cellReference.getSheetName(), cellReference.getRow(),
                cellReference.getCol());
    }

    /**
     * Create cell address with optional sheet reference.
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param coordinates are coordinates of cell on sheet
     */
    CellAddressImpl(@Nullable String sheetName, CellCoordinates coordinates) {
        if (sheetName != null) {
            if (sheetName.equals("")) {
                throw new RuntimeException("Empty sheet name sent to CellAddress constructor");
            }
        }
        this.sheetName = sheetName;
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Create cell address with optional sheet reference. Only producess full references (e.g. neither column nor row
     * missing)
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     */
    CellAddressImpl(@Nullable String sheetName, int row, int col) {
        this(sheetName, new CellCoordinatesImpl(row, col));
    }

    /**
     * Create cell address without sheet reference. It is used when referencing cells on the same sheet
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     */
    CellAddressImpl(int row, int col) {
        this(null, row, col);
    }

    /**
     * Create cell address without reference.
     *
     * @param coordinates are coordinates of cell on sheet
     */
    CellAddressImpl(CellCoordinates coordinates) {
        this(null, coordinates);
    }

    @Override
    public Optional<String> getSheetName() {
        return Optional.ofNullable(sheetName);
    }

    @Override
    public CellCoordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public int getRow() {
        return coordinates.getRow();
    }

    @Override
    public int getCol() {
        return coordinates.getCol();
    }

    @Override
    public CellAddress shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellAddressImpl(sheetName, getRow() + rowShift, getCol() + colShift);
    }

    @Override
    public CellAddress shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }
}
