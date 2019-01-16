package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Represents reference to cell in formula, named area etc. It contains cell address and absolute / relative modifiers
 * for row and cell coordinates
 */
public interface CellReference extends CellAddress {

    /**
     * @return if row position is absolute (prefixed with $)
     */
    boolean isRowAbsolute();

    /**
     * @return if column position is absolute (prefixed with $)
     */
    boolean isColAbsolute();

    /**
     * Get cell reference shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellReference shifted by specified offset; returns empty optional if resulting coordinates are not valid
     * (e.g. would have negative row or column index)
     */
    @Override
    @Nonnull
    Optional<? extends CellReference> shiftByOrEmpty(int rowShift, int colShift);

    /**
     * Get cell reference shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellAddress shifted by specified offset
     */
    @Override
    @Nonnull
    CellReference shiftBy(int rowShift, int colShift);

    /**
     * Get cell reference shifted by offset, specified by another CellAddress.
     *
     * @param shift is cell address taken as shift offset. If supplied coordinates are in fact address, it must either
     *             have same sheet as cell address it is applied to or no sheet; especially if base cell address has no
     *             sheet, supplied cell address also cannot have sheet
     * @return cell address shifted by specified offset
     */
    @Override
    @Nonnull
    CellReference shiftBy(CellCoordinates shift);
}
