package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.repexecutor.CellAddress;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellReference;

import java.util.Objects;
import java.util.Optional;

public class CellAddressImpl implements CellAddress {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private final Optional<String> sheetName;
    private final int row;
    private final int col;

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
        return new CellAddressImpl(Optional.ofNullable(cellReference.getSheetName()), cellReference.getRow(),
                cellReference.getCol());
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
    public CellAddressImpl(Optional<String> sheetName, int row, int col) {
        this.sheetName = Objects.requireNonNull(sheetName);
        if (sheetName.isPresent()) {
            if (sheetName.get().equals("")) {
                throw new RuntimeException("Empty sheet name sent to CellAddress constructor");
            }
        }
        if (row < 0) {
            LOG.error("Row index must be positive, not {}", row);
            throw new IllegalArgumentException("Row index must be positive");
        }
        this.row = row;
        if (col < 0) {
            LOG.error("Column index must be positive, not {}", col);
            throw new IllegalArgumentException("Column index must be positive");
        }
        this.col = col;
    }

    /**
     * Create cell address from specified sheet name, row and column.
     *
     * @param sheetName is name of sheet this object addresses. Cannot be null nor empty - use other form of constructor
     *                  to create reference relative in sheet
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     */
    CellAddressImpl(String sheetName, int row, int col) {
        this(Optional.of(sheetName), row, col);
    }

    /**
     * Create cell address without sheet reference. It is used when referencing cells on the same sheet
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     */
    CellAddressImpl(int row, int col) {
        this(Optional.empty(), row, col);
    }

    @Override
    public Optional<String> getSheetName() {
        return sheetName;
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
    public CellAddress shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellAddressImpl(sheetName, row + rowShift, col + colShift);
    }
}
