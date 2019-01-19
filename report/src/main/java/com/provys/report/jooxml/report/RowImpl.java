package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.RowProperties;

import java.util.*;

public class RowImpl implements Row {

    final private int rowIndex;
    final private RowProperties rowProperties;
    final private List<AreaCell> cells;

    RowImpl(int rowIndex, RowProperties rowProperties, Collection<AreaCell> cells) {
        this.rowIndex = rowIndex;
        this.rowProperties = rowProperties;
        this.cells = new ArrayList<>(cells);
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public RowProperties getRowProperties() {
        return rowProperties;
    }

    @Override
    public List<AreaCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    @Override
    public Iterator<AreaCell> iterator() {
        return Collections.unmodifiableList(cells).iterator();
    }

}
