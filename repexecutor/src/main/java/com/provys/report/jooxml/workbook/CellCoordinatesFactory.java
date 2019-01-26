package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;

/**
 * Provides access to new instances of cell coordinates, create either by explicit specification of coordinates or
 * parsing coordinates supplied in Excel cell reference format
 */
public interface CellCoordinatesFactory {

    /**
     * Get cell coordinates with given address.
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     * @return instance of CellCoordinates with given coordinates
     * @throws IllegalArgumentException if either coordinate is negative
     */
    @Nonnull
    CellCoordinates of(int row, int col);

    /**
     * Parse supplied address and convert it to cell coordinates
     *
     * @param address is cell address (as in Excel - e.g. A1)
     * @return cell coordinates corresponding to supplied address
     * @throws IllegalArgumentException if supplied address is not valid Excel cell reference, without sheet and with
     * no absolute position
     */
    @Nonnull
    CellCoordinates parse(String address);
}
