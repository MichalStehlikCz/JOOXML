package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import java.util.Objects;

public class CellValueFormula implements CellValue {

    private final String formula;

    CellValueFormula(String formula) {
        if (formula.isEmpty()) {
            throw new IllegalArgumentException("Formula cannot be empty string");
        }
        this.formula = formula;
    }

    @Override
    public CellType getCellType() {
        return CellType.FORMULA;
    }

    /**
     * @return formula used in given value
     */
    public String getFormula() {
        return formula;
    }

    @Override
    public boolean equals(Object o) {
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
    public String toString() {
        return "CellValueFormula{formula=\"" + getFormula() + "\"}";
    }
}
