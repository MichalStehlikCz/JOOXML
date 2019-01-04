package com.provys.report.jooxml.workbook;

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
     * Get cell coordinates shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellAddress shifted by specified offset
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
