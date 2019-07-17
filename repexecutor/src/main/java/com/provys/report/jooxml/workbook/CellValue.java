package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CellValue {

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
     * Function retrieves date value. Should only be used on numeric type cell.
     *
     * @return date value for numeric cell
     * @throw IllegalArgumentException if cell is not numeric cell
     */
    @Nonnull
    default Optional<LocalDateTime> getDateValue() {
        throw new IllegalArgumentException("Cannot access date value - cell value is of wrong type");
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
