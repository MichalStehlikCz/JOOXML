package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Gives access to new instances of CellValues of various types
 */
public interface CellValueFactory {

    /**
     * Creates new cell value of formula type from supplied string.
     *
     * @param formula is formula used in cell value
     * @return cell value with given formula
     */
    @Nonnull
    CellValue ofFormula(String formula);

    /**
     * Creates new cell value of string type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return string cell value with given value
     */
    @Nonnull
    CellValue ofString(@Nullable String value);

    /**
     * Creates new cell value of numeric type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return numeric cell value with given value
     */
    @Nonnull
    CellValue ofNumeric(@Nullable Double value);

    /**
     * Creates new cell value of boolean type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return boolean cell value with given value
     */
    @Nonnull
    CellValue ofBoolean(@Nullable Boolean value);

    /**
     * Creates new cell value of error type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return error cell value with given value
     */
    @Nonnull
    CellValue ofError(@Nullable Byte value);

    /**
     * Gets blank cell value.
     *
     * @return blank cell value
     */
    @Nonnull
    CellValue getBlank();

}
