package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CellPropertiesInt implements CellProperties {

    private final Integer styleIndex;

    /**
     * Return properties with specified style index.
     *
     * @param styleIndex is style index properties should use
     * @return propertiss with given characteristics
     */
    @Nonnull
    public static CellPropertiesInt of(@Nullable Integer styleIndex) {
        return new CellPropertiesInt(styleIndex);
    }

    CellPropertiesInt(@Nullable Integer styleIndex) {
        this.styleIndex = styleIndex;
    }

    @Override
    @Nonnull
    public Optional<Integer> getStyleIndex() {
        return Optional.ofNullable(styleIndex);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPropertiesInt that = (CellPropertiesInt) o;
        return Objects.equals(getStyleIndex(), that.getStyleIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStyleIndex());
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellPropertiesInt{" +
                "styleIndex=" + getStyleIndex().map(Object::toString).orElse("null") +
                '}';
    }
}
