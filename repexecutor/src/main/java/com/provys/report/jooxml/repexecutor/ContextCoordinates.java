package com.provys.report.jooxml.repexecutor;

import java.util.Objects;

public class ContextCoordinates {
    private RepWSheet sheet;
    private int rowIndex;
    private int columnIndex;

    ContextCoordinates(RepWSheet sheet, int rowIndex, int columnIndex) {
        this.sheet = Objects.requireNonNull(sheet);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public RepWSheet getSheet() {
        return sheet;
    }

    public void incRowBy(int height) {
        rowIndex = rowIndex + height;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
