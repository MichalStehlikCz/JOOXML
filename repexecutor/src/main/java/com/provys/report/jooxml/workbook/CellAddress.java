package com.provys.report.jooxml.workbook;

import java.util.Optional;

/**
 * Holds cell coordinates (sheet, row and column indices)
 */
public interface CellAddress {
    /**
     * @return name of sheet cell should be placed on; might be empty, in that case sheet should be inferred from
     * context
     */
    Optional<String> getSheetName();

    /**
     * @return row index of cell; indices are zero-based (e.g. one lower than row number displayed in excel sheet)
     */
    int getRow();

    /**
     * @return column index of cell; indices are zero based
     */
    int getCol();

    /**
     * Get cell reference shifted by specified offset
     *
     * @param rowShift is row offset
     * @param colShift is column offset
     * @return CellAddress shifted by specified offset
     */
    CellAddress shiftBy(int rowShift, int colShift);

    /**
     * Get cell reference shifted by offset, specified by another CellAddress.
     *
     * @param shift is cell address taken as shift offset. Supplied cell addres must wither have same sheet as cell
     *              reference it is applied on or no sheet; especially if base cell address has no sheet, supplied
     *              cell address also cannot have sheet
     * @return cell address shifted by specified offset
     */
    CellAddress shiftBy(CellAddress shift);
}