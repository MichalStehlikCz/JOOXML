package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Class represents address of cell in report - going from root region through all subregions to region that actually
 * populates cell with record number in each region
 */
public class CellPathRegion implements CellPath {
    @Nonnull
    private final CellPath childPath;
    @Nonnull
    private final String regionNm;

    public CellPathRegion(CellPath childPath, String regionNm) {
        this.childPath = Objects.requireNonNull(childPath);
        this.regionNm = Objects.requireNonNull(regionNm);
    }

    @Nonnull
    CellPath getChildPath() {
        return childPath;
    }

    @Nonnull
    String getRegionNm() {
        return regionNm;
    }
}
