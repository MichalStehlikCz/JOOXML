package com.provys.report.jooxml.repexecutor;

import org.apache.poi.ss.util.CellReference;

import java.util.Objects;

public class CellPathCell implements CellPath {
    final CellReference cell;

    public CellPathCell(CellReference cell) {
        this.cell = Objects.requireNonNull(cell);
    }

    public CellReference getCellReference() {
        return cell;
    }
}
