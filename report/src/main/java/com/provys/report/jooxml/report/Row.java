package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.RowProperties;

import java.util.List;

/**
 * Represents data row in region. Supports iteration through row's cells
 */
public interface Row extends Iterable<AreaCell> {

    /**
     * @return row index of given row; rows are numbered from 0 within region
     */
    int getRowIndex();

    /**
     * @return row properties
     */
    RowProperties getRowProperties();

    /**
     * Returns list of cells in row
     */
    List<AreaCell> getCells();
}
