package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.RowProperties;

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
