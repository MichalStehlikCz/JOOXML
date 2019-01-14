package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellProperties;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

class CellPropertiesImpl implements CellProperties {

    private final Integer styleIndex;

    CellPropertiesImpl(@Nullable Integer styleIndex) {
        this.styleIndex = styleIndex;
    }

    @Override
    public Optional<Integer> getStyleIndex() {
        return Optional.ofNullable(styleIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPropertiesImpl that = (CellPropertiesImpl) o;
        return Objects.equals(getStyleIndex(), that.getStyleIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStyleIndex());
    }

    @Override
    public String toString() {
        return "CellPropertiesImpl{" +
                "styleIndex=" + getStyleIndex().map(Object::toString).orElse("null") +
                '}';
    }
}
