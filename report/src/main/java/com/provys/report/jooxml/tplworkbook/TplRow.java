package com.provys.report.jooxml.tplworkbook;

import com.provys.report.jooxml.workbook.RowProperties;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public interface TplRow extends Iterable<TplCell> {

    /**
     * Return row index.
     *
     * @return row index of given row; note that row indexes are going from 0, unlike row numbers in excel tht are 1
     * based
     */
    int getRowIndex();

    /**
     * Return cell with given index.
     *
     * @param colIndex index of cell to be retrieved (starting from 0)
     * @return specified cell if it exists, empty optional if such cell does not exist in given row or is blank
     */
    @Nonnull
    Optional<TplCell> getCell(int colIndex);

    /**
     * Return read-only collection of cells; only existing cells are returned.
     */
    @Nonnull
    Collection<TplCell> getCells();

    /**
     * @return row properties for given row
     */
    @Nonnull
    RowProperties getProperties();
}
