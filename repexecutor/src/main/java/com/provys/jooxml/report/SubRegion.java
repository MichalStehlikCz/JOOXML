package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import com.provys.jooxml.repexecutor.ReportSubRegion;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Subregion wraps region and adds offset against parent region / previous subregion.
 */
class SubRegion implements ReportSubRegion {

    private final int offset;
    private final ReportRegion region;

    SubRegion (int offset, ReportRegion region) {
        this.offset = offset;
        this.region = Objects.requireNonNull(region);
    }

    /**
     * @return offset against parent region (0 = subregion starts at the start of parent region) or against previous
     * subregion (0 = subregion starts on row / column next to last row / column of previous subregion)
     */
    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String getNameNm() {
        return region.getNameNm();
    }

    @Override
    public List<ReportRegionRow> getRows() {
        return region.getRows();
    }

    @Override
    public int getHeight() {
        return region.getHeight();
    }

    @Override
    public Iterator<? extends ReportSubRegion> subRegions() {
        return region.subRegions();
    }

    @Override
    public List<? extends ReportSubRegion> getSubRegions() {
        return region.getSubRegions();
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return region.getReportDataSource();
    }

    @Override
    public Iterator<ReportSubRegion> iterator() {
        return region.iterator();
    }
}
