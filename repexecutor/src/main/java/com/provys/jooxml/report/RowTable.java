package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;

import java.util.Collection;

/**
 * Row table is area that is repeated for each row in dataset. It requires to own dataset as it will iterate through
 * rows.
 */
class RowTable extends RowRegion {

    /**
     * Constructor for row report region. Use RwTableBuilder to prepare parameters for constructor.
     * Does not verify that parameters are valid - it expects that this validation was performed by builder.
     *
     * @param nameNm is internal name of this report region, unique within parent
     * @param dataSource is data source this region takes data from
     * @param height     is height of row area (in rows)
     * @param rows       are row objects containing cells to be initialized in given region. Note that number of rows might
     *                   be actually lower than specified height as empty rows can be ommited
     * @param subRegions is collection of subregions, ordered by their first row
     */
    RowTable(String nameNm, ReportDataSource dataSource, int height, Collection<ReportRegionRow> rows
            , Collection<ReportRegion> subRegions) {
        super(nameNm, dataSource, height, rows, subRegions);
    }
}
