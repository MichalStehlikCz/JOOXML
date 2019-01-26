package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CellValueNumeric extends CellValueBase<Double> {

    /**
     * Creates new cell value of numeric type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return numeric cell value with given value
     */
    @Nonnull
    public static CellValueNumeric of(@Nullable Double value) {
        return new CellValueNumeric(value);
    }

    private CellValueNumeric(@Nullable Double value) {
        super(value);
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Nonnull
    @Override
    public Optional<Double> getNumericValue() {
        return getValue();
    }

}
