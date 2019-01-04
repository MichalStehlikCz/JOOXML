package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellValue;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

abstract public class CellValueBase<T> implements CellValue {

    @Nullable
    private final T value;

    CellValueBase(@Nullable T value) {
        this.value = value;
    }

    /**
     * @return optional value of cell
     */
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellValueString that = (CellValueString) o;

        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    /**
     * @return string representation of object in form{@literal <class>(value=<value>)}. null in value is represented as
     * null literal
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "value=" + getValue().map(T::toString).orElse("null") +
                '}';
    }
}
