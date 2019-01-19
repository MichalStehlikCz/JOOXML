package com.provys.report.jooxml.workbook.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

abstract class CellValueBase<T> implements CellValueInt {

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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellValueBase that = (CellValueBase) o;

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
    @Nonnull
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "value=" + getValue().map(T::toString).orElse("null") +
                '}';
    }
}
