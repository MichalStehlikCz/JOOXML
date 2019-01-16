package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CellValueInt extends CellValue {

    /**
     * Creates new cell value of formula type from supplied string.
     *
     * @param formula is formula used in cell value
     * @return cell value with given formula
     */
    @Nonnull
    static CellValueInt ofFormula(String formula) {
        return CellValueFormula.of(formula);
    }

    /**
     * Creates new cell value of string type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return string cell value with given value
     */
    @Nonnull
    static CellValueInt ofString(@Nullable String value) {
        return CellValueString.of(value);
    }

    /**
     * Creates new cell value of numeric type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return numeric cell value with given value
     */
    @Nonnull
    static CellValueInt ofNumeric(@Nullable Double value) {
        return CellValueNumeric.of(value);
    }

    /**
     * Creates new cell value of boolean type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return boolean cell value with given value
     */
    @Nonnull
    static CellValueInt ofBoolean(@Nullable Boolean value) {
        return CellValueBoolean.of(value);
    }

    /**
     * Creates new cell value of error type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return error cell value with given value
     */
    @Nonnull
    static CellValueInt ofError(@Nullable Byte value) {
        return CellValueError.of(value);
    }

    /**
     * Gets blank cell value.
     *
     * @return blank cell value
     */
    @Nonnull
    static CellValueInt ofBlank() {
        return CellValueBlank.get();
    }

}
