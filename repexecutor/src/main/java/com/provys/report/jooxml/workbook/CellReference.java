package com.provys.report.jooxml.workbook;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;

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

    /**
     * Get cell reference, shifted bz offset, defined by {@code ContextCoordinates}
     *
     * @param shift is coordinate defining upper left corner of area being filled in, where cell reference is considered
     *              to be filled. Sheet is either ignored (if it is not present in this reference) as reference within
     *              sheet is shifted to reference within sheet, or sheet from shift parameter is used as target
     * @return cell reference corresponding to position with coordinate this relative to area start shift
     */
    @Nonnull
    CellReference shiftBy(ContextCoordinates shift);
}
