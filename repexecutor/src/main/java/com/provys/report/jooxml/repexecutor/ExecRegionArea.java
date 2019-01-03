package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellReference;

import java.util.Optional;

public class ExecRegionArea implements ExecRegion {

    private final int firstRow;
    private final int firstColumn;

    ExecRegionArea(int firstRow, int firstColumn) {
        if (firstRow < 0) {
            throw new IllegalArgumentException("First row coordinate cannot be negative");
        }
        this.firstRow = firstRow;
        if (firstColumn < 0) {
            throw new IllegalArgumentException("First column coordinte cannot be negative");
        }
        this.firstColumn = firstColumn;
    }

    CellAddress getCell(CellAddress relativeAddress) {
        return relativeAddress.shiftBy(firstRow, firstColumn);
    }

    @Override
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathCell)) {
            throw new IllegalArgumentException("Cannot evaluate CellPath - Cell expected in lowest level region");
        }
        return Optional.of(((CellPathCell) path).getCellReference().shiftBy(firstColumn, firstRow));
    }
}
