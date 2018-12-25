package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;

import java.util.Collection;

/**
 * Represents area for single row dataset. Does not iterate through rows, returned by dataset. If dataset is already
 * opened, uses current line; in this case, it does not matter if dataset returns single or multiple lines.
 * Otherwise opens dataset and fetches rows; if more than one line is fetched, throws an exception.
 */
class RowArea extends RowRegion {

    /**
     * Constructor for row report area. Use RowAreaBuilder to prepare parameters for constructor.
     * Does not verify that parameters are valid - it expects that this validation was performed by builder.
     *
     * @param dataSource is data source for this area
     * @param height     is height of row area (in rows)
     * @param rows       are row objects containing cells to be initialized in given region. Note that number of rows might
     *                   be actually lower than specified height as empty rows can be ommited
     * @param subRegions is collection of subregions, ordered by their first row
     */
    RowArea(ReportDataSource dataSource, int height, Collection<ReportRegionRow> rows
            , Collection<ReportRegion> subRegions) {
        super(dataSource, height, rows, subRegions);
    }
}
