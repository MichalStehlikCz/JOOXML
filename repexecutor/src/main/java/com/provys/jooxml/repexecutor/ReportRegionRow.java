package com.provys.jooxml.repexecutor;

import java.util.List;

/**
 * Represents data row in region. Supports iteration through row's cells
 */
public interface ReportRegionRow extends Iterable<ReportRegionCell> {

    /**
     * @return row index of given row; rows are numbered from 0 within region
     */
    int getRowIndex();

    /**
     * Returns list of cells in row
     */
    List<ReportRegionCell> getCells();
}
