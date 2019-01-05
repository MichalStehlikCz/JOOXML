package com.provys.report.jooxml.workbook;

import java.util.Optional;

public interface CellCoordinates {

    /**
     * @return row index of cell; indices are zero-based (e.g. one lower than row number displayed in excel sheet)
     */
    int getRow();

    /**
     * @return column index of cell; indices are zero based
     */
    int getCol();

    /**
     * Append address (as used in excel - e.g. A1) to string builder
     *
     * @param builder is string builder cell reference should be appended to
     */
    void appendAddress(StringBuilder builder);

    /**
     * @return address as used in Excel (e.g. column identified by letter + one-based row number
     */
    String getAddress();

    /**
     * Get cell coordinates shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellAddress shifted by specified offset; returns empty optional if resulting coordinates are not valid
     * (e.g. would have negative row or column index)
     */
    Optional<CellCoordinates> shiftByOrEmpty(int rowShift, int colShift);

    /**
     * Get cell coordinates shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellAddress shifted by specified offset
     * @throws IllegalArgumentException when resulting coordinates are not valid (negative row or column index)
     */
    CellCoordinates shiftBy(int rowShift, int colShift);

    /**
     * Get cell coordinates shifted by offset, specified by another CellCoordinates.
     *
     * @param shift are cell coordinates taken as shift offset.
     * @return cell address shifted by specified offset
     */
    CellCoordinates shiftBy(CellCoordinates shift);

}
