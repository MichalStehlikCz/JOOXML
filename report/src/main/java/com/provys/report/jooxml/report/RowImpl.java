package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.RowProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RowImpl implements Row {

    final private int rowIndex;
    @Nullable
    final private RowProperties rowProperties;
    @Nonnull
    final private List<AreaCell> cells;

    RowImpl(int rowIndex, @Nullable RowProperties rowProperties, Collection<AreaCell> cells) {
        this.rowIndex = rowIndex;
        this.rowProperties = rowProperties;
        this.cells = new ArrayList<>(cells);
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Nonnull
    @Override
    public Optional<RowProperties> getRowProperties() {
        return Optional.ofNullable(rowProperties);
    }

    @Nonnull
    @Override
    public List<AreaCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    @Nonnull
    @Override
    public Iterator<AreaCell> iterator() {
        return Collections.unmodifiableList(cells).iterator();
    }

}
