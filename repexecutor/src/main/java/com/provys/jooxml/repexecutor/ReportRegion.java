package com.provys.jooxml.repexecutor;

import java.util.*;

/**
 * Describes report region and enables its usage for pushing data to target worksheet.
 * All coordinates in report region are relative to parent region template area.
 */
public interface ReportRegion extends Iterable<ReportRegion> {
    /**
     * @return list of data rows, applicable on area
     */
    List<ReportRegionRow> getRows();

    /**
     * @return height of this region
     */
    int getHeight();

    /**
     * Iterator for iteration through subregions
     */
    Iterator<ReportRegion> subRegions();

    /**
     * @return subregions as list
     */
    List<ReportRegion> getSubRegions();

    /**
     * @return data source used to populate this region
     */
    ReportDataSource getReportDataSource();
}
