package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class CellValueFormula implements CellValueInt {

    @Nonnull
    private final String formula;

    /**
     * Retrieve formula cell value with specified formula
     *
     * @param formula is formula to be used in cell value
     * @return formula cell value with given characteristics
     */
    @Nonnull
    public static CellValueFormula of(String formula) {
        return new CellValueFormula(formula);
    }

    private CellValueFormula(String formula) {
        if (formula.isEmpty()) {
            throw new IllegalArgumentException("Formula cannot be empty string");
        }
        this.formula = formula;
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.FORMULA;
    }

    /**
     * @return formula used in given value
     */
    @Override
    @Nonnull
    public String getFormula() {
        return formula;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellValueFormula that = (CellValueFormula) o;
        return Objects.equals(getFormula(), that.getFormula());
    }

    @Override
    public int hashCode() {
        return getFormula().hashCode();
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellValueFormula{formula=\"" + getFormula() + "\"}";
    }
}
