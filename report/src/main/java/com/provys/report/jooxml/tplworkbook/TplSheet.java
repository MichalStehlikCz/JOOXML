package com.provys.report.jooxml.tplworkbook;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface defines functionality, required from sheet in template workbook by JOOXML report executor.
 */
public interface TplSheet extends Iterable<TplRow> {

    /**
     * Method allows to retrieve row with specified number form worksheet.
     *
     * @param rowIndex index of row to be retrieved (starting from 0, e.g. offseted by 1 against natural row number used
     *              by Excel)
     * @return specified row if it exists, empty optional if such row does not exist on given sheet
     */
    @Nonnull
    Optional<TplRow> getRow(int rowIndex);

    /**
     * Retrieve rows in given range (both bounds inclusive).
     *
     * @param firstRow index of first row to be returned (counted from 0, e.g. one smaller than number of row displayed
     *                in Excel)
     * @param lastRow index of last row to be returned
     * @return collection containing rows in given range
     */
    @Nonnull
    Collection<TplRow> getRows(int firstRow, int lastRow);
}
