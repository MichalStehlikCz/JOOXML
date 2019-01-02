package com.provys.report.jooxml.workbook;

/**
 * Represents reference to cell in formula, named area etc.
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
     * @return CellAddress shifted by specified offset
     */
    @Override
    CellReference shiftBy(int rowShift, int colShift);
}
