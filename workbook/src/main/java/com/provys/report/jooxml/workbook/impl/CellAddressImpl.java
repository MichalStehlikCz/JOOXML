package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellReference;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CellAddressImpl implements CellAddress {

    private static final Logger LOG = LogManager.getLogger(CellAddressImpl.class.getName());
    private final String sheetName;
    private final CellCoordinates coordinates;

    static CellAddressImpl parse(String address) {
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
        return new CellAddressImpl(cellReference.getSheetName(),
                Workbooks.getCellCoordinates(cellReference.getRow(), cellReference.getCol()));
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
        return new CellAddressImpl(sheetName,
                Workbooks.getCellCoordinates(getRow() + rowShift, getCol() + colShift));
    }

    @Override
    public CellAddress shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }
}
