package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface CellValue {

    /**
     * Creates new cell value of formula type from supplied string.
     *
     * @param formula is formula used in cell value
     * @return cell value with given formula
     */
    @Nonnull
    static CellValue ofFormula(String formula) {
        return Workbooks.getFormulaValue(formula);
    }

    /**
     * Creates new cell value of string type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return string cell value with given value
     */
    @Nonnull
    static CellValue ofString(@Nullable String value) {
        return Workbooks.getStringValue(value);
    }

    /**
     * Creates new cell value of numeric type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return numeric cell value with given value
     */
    @Nonnull
    static CellValue ofNumeric(@Nullable Double value) {
        return Workbooks.getNumericValue(value);
    }

    /**
     * Creates new cell value of boolean type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return boolean cell value with given value
     */
    @Nonnull
    static CellValue ofBoolean(@Nullable Boolean value) {
        return Workbooks.getBooleanValue(value);
    }

    /**
     * Creates new cell value of error type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return error cell value with given value
     */
    @Nonnull
    static CellValue ofError(@Nullable Byte value) {
        return Workbooks.getErrorValue(value);
    }

    /**
     * Gets blank cell value.
     *
     * @return blank cell value
     */
    @Nonnull
    static CellValue ofBlank() {
        return Workbooks.getBlankValue();
    }

    /**
     * @return type of cell value
     */
    @Nonnull
    CellType getCellType();

    /**
     * Function retrieves formula value. Should only be used on formula type cell.
     *
     * @return formula value for formula cell
     * @throw IllegalArgumentException if cell is not formula cell
     */
    @Nonnull
    default String getFormula() {
        throw new IllegalArgumentException("Cannot access formula value - cell value is of wrong type");
    }

    /**
     * Function retrieves string value. Should only be used on string type cell.
     *
     * @return string value for string cell
     * @throw IllegalArgumentException if cell is not string cell
     */
    @Nonnull
    default Optional<String> getStringValue() {
        throw new IllegalArgumentException("Cannot access string value - cell value is of wrong type");
    }

    /**
     * Function retrieves numeric value. Should only be used on numeric type cell.
     *
     * @return numeric value for numeric cell
     * @throw IllegalArgumentException if cell is not numeric cell
     */
    @Nonnull
    default Optional<Double> getNumericValue() {
        throw new IllegalArgumentException("Cannot access numeric value - cell value is of wrong type");
    }

    /**
     * Function retrieves boolean value. Should only be used on boolean type cell.
     *
     * @return boolean value for boolean cell
     * @throw IllegalArgumentException if cell is not boolean cell
     */
    @Nonnull
    default Optional<Boolean> getBooleanValue() {
        throw new IllegalArgumentException("Cannot access boolean value - cell value is of wrong type");
    }

    /**
     * Function retrieves error value. Should only be used on error type cell.
     *
     * @return error value for error cell
     * @throw IllegalArgumentException if cell is not formula cell
     */
    @Nonnull
    default Optional<Byte> getErrorValue() {
        throw new IllegalArgumentException("Cannot access error value - cell value is of wrong type");
    }

}
