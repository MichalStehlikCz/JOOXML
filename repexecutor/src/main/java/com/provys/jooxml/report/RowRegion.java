package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import org.apache.poi.ss.util.CellReference;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines region in JOOXML report. Supports common functions required by all row region implementations.
 */
abstract class RowRegion implements ReportRegion {

    private final ReportDataSource dataSource;
    private final int height;
    private final List<ReportRegionRow> rows;
    private final List<ReportRegion> subRegions;

    /**
     * Constructor for row report region. Use RowRegionBuilder to prepare parameters for report region constructor.
     * Does not verify that parameters are valid - it expects that this validation was performed by builder.
     *
     * @param height is height of row area (in rows)
     * @param rows are row objects containing cells to be initialized in given region. Note that number of rows might
     *             be actually lower than specified height as empty rows can be ommited
     * @param subRegions is collection of subregions, ordered by their first row
     */
    RowRegion(ReportDataSource dataSource, int height, Collection<ReportRegionRow> rows
            , Collection<ReportRegion> subRegions) {
        this.dataSource = dataSource;
        this.height = height;
        this.rows = new ArrayList<> (rows);
        this.subRegions = new ArrayList<> (subRegions);
    }

    @Override
    public List<ReportRegionRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Iterator<ReportRegion> subRegions() {
        return Collections.unmodifiableList(subRegions).iterator();
    }

    @Override
    public Iterator<ReportRegion> iterator() {
        return subRegions();
    }

    @Override
    public List<ReportRegion> getSubRegions() {
        return Collections.unmodifiableList(subRegions);
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return dataSource;
    }

}
