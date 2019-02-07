package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.RowProperties;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

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
    @Nonnull
    Optional<RowProperties> getRowProperties();

    /**
     * Returns list of cells in row
     */
    @Nonnull
    List<AreaCell> getCells();
}
