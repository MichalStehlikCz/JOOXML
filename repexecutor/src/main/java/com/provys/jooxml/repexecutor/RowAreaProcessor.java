package com.provys.jooxml.repexecutor;

import java.util.Objects;

public class RowAreaProcessor {
    final private ReportRegion region;
    final private RegionProcessor parent;
    final private int firstRow;
    final private int firstColumn;
    final private ExecRegionRegion execRegion;
    int currentRow;

    public RowAreaProcessor(ReportRegion region, RegionProcessor parent, int firstRow, int firstColumn) {
        this.region = Objects.requireNonNull(region);
        this.parent = parent;
        this.firstRow = firstRow;
        this.firstColumn = firstColumn;
        this.execRegion = new ExecRegionRegion(region, 1, this.firstRow, this.firstColumn);
    }

    private void processRecord() {
    }

    public void process() {
        processRecord();
    }
}
