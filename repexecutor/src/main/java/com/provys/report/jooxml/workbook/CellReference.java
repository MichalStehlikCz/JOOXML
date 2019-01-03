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

    /**
     * Get cell reference shifted by offset, specified by another CellAddress.
     *
     * @param shift is cell address taken as shift offset. If supplied coordinates are in fact address, it must either
     *             have same sheet as cell address it is applied to or no sheet; especially if base cell address has no
     *             sheet, supplied cell address also cannot have sheet
     * @return cell address shifted by specified offset
     */
    CellReference shiftBy(CellCoordinates shift);
}
