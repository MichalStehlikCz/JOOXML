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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellPathRegion)) return false;
        CellPathRegion that = (CellPathRegion) o;
        return getChildPath().equals(that.getChildPath()) &&
                getRegionNm().equals(that.getRegionNm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChildPath(), getRegionNm());
    }

    @Override
    public String toString() {
        return "CellPathRegion{" +
                "childPath=" + childPath +
                ", regionNm='" + regionNm + '\'' +
                '}';
    }
}
