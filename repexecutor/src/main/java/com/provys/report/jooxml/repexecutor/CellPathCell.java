package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellAddress;

import java.util.Objects;

public class CellPathCell implements CellPath {
    final CellAddress cell;

    public CellPathCell(CellAddress cell) {
        this.cell = Objects.requireNonNull(cell);
    }

    public CellAddress getCellAddress() {
        return cell;
    }
}
