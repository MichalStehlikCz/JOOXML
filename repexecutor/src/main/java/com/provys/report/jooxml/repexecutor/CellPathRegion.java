package com.provys.report.jooxml.repexecutor;

import java.util.Objects;

/**
 * Class represents address of cell in report - going from root region through all subregions to region that actually
 * populates cell with record number in each region
 */
public class CellPathRegion implements CellPath {
    private final CellPath childPath;
    private final String region;

    public CellPathRegion(CellPath childPath, ReportStep region, int recordNr) {
        this.childPath = Objects.requireNonNull(childPath);
        this.region = region.getNameNm();
        if (recordNr < 0) {
            throw new IllegalArgumentException("Record number cannot be negative");
        }
    }

    public CellPath getChildPath() {
        return childPath;
    }

    public String getRegion() {
        return region;
    }
}
