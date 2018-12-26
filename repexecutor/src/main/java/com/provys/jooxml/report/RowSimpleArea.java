package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import com.provys.jooxml.repexecutor.ReportSubRegion;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class RowSimpleArea implements ReportRegion {

    private final String nameNm;
    private final int height;
    private final List<ReportRegionRow> rows;

    @Override
    public String getNameNm() {
        return nameNm;
    }

    public List<ReportRegionRow> getRows() {
        return null;
    }

    public int getHeight() {
        return 0;
    }

    public Iterator<? extends ReportSubRegion> subRegions() {
        return null;
    }

    public List<? extends ReportSubRegion> getSubRegions() {
        return null;
    }

    @Override
    public Optional<ReportDataSource> getDataSource() {
        return null;
    }

    @Override
    public Iterator<ReportSubRegion> iterator() {
        return null;
    }
}
