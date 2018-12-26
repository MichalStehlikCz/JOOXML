package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import com.provys.jooxml.repexecutor.ReportSubRegion;

import java.util.*;

/**
 * Defines region in JOOXML report. Supports common functions required by all row region implementations.
 */
abstract class RowRegion implements ReportRegion {

    private final String nameNm;
    private final Optional<ReportDataSource> dataSource;
    private final int height;
    private final List<ReportRegionRow> rows;
    private final List<ReportSubRegion> subRegions;

    /**
     * Constructor for row report region. Use RowRegionBuilder to prepare parameters for report region constructor.
     * Does not verify that parameters are valid - it expects that this validation was performed by builder.
     *
     * @param height is height of row area (in rows)
     * @param rows are row objects containing cells to be initialized in given region. Note that number of rows might
     *             be actually lower than specified height as empty rows can be ommited
     * @param subRegions is collection of subregions, ordered by their first row
     */
    RowRegion(String nameNm, Optional<ReportDataSource> dataSource, int height, Collection<ReportRegionRow> rows
            , Collection<ReportSubRegion> subRegions) {
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name of region cannot be empty");
        }
        this.nameNm = nameNm;
        this.dataSource = dataSource;
        this.height = height;
        this.rows = new ArrayList<>(rows);
        this.subRegions = new ArrayList<> (subRegions);
    }

    @Override
    public String getNameNm() {
        return nameNm;
    }

    public List<ReportRegionRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public int getHeight() {
        return height;
    }

    public Iterator<ReportSubRegion> subRegions() {
        return Collections.unmodifiableList(subRegions).iterator();
    }

    @Override
    public Iterator<ReportSubRegion> iterator() {
        return subRegions();
    }

    public List<? extends ReportSubRegion> getSubRegions() {
        return Collections.unmodifiableList(subRegions);
    }

    @Override
    public Optional<ReportDataSource> getDataSource() {
        return dataSource;
    }

}
