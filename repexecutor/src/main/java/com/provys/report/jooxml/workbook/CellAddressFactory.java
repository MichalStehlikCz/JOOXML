package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Gives access to new instances of CellAddress type
 */
public interface CellAddressFactory {

    /**
     * Create cell address with optional sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param row is row index of cell (zero based)
     * @param col is column index of cell (zero based)
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty or creation of cell coordinates fails (e.g.
     * row or column are negative)
     */
    @Nonnull
    CellAddress of(@Nullable String sheetName, int row, int col);

    /**
     * Create cell address without sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param row is row index of cell (zero based)
     * @param col is column index of cell (zero based)
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty or creation of cell coordinates fails (e.g.
     * row or column are negative)
     */
    @Nonnull
    CellAddress of(int row, int col);

    /**
     * Create cell address with optional sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param coordinates are coordinates of cell on sheet
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty
     */
    @Nonnull
    CellAddress of(@Nullable String sheetName, CellCoordinates coordinates);

    /**
     * Create cell address - variant without sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param coordinates are coordinates of cell on sheet
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty
     */
    @Nonnull
    CellAddress of(CellCoordinates coordinates);

    /**
     * Parse supplied address and convert it to {@code CellAddress}
     *
     * @param address is cell address (as in Excel - e.g. A1), including optional sheet name
     * @return cell coordinates corresponding to supplied address
     * @throws IllegalArgumentException if supplied address is not valid Excel cell reference, with no absolute position
     */
    @Nonnull
    CellAddress parse(String address);
}
