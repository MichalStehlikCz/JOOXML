package com.provys.jooxml.repexecutor;

import org.apache.poi.ss.util.CellReference;

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

    CellReference getCell(int cellRow, int cellColumn) {
        return new CellReference(cellRow + firstRow, cellColumn + firstColumn);
    }

    @Override
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathCell)) {
            throw new IllegalArgumentException("Cannot evaluate CellPath - Cell expected in lowest level region");
        }
        CellReference cell = ((CellPathCell) path).getCellReference();
        return Optional.of(new CellReference(cell.getSheetName(), cell.getRow() + firstRow
                , cell.getCol() + firstColumn, cell.isRowAbsolute(), cell.isColAbsolute()));
    }
}
