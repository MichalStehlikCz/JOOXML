package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.Comment;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CellPropertiesImpl implements CellProperties {

    private final Integer styleIndex;
    private final Comment comment;

    CellPropertiesImpl(@Nullable Integer styleIndex, @Nullable Comment comment) {
        this.styleIndex = styleIndex;
        this.comment = comment;
    }

    @Override
    public Optional<Integer> getStyleIndex() {
        return Optional.ofNullable(styleIndex);
    }

    @Override
    public Optional<Comment> getComment() {
        return Optional.ofNullable(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPropertiesImpl that = (CellPropertiesImpl) o;
        return Objects.equals(getStyleIndex(), that.getStyleIndex()) &&
                Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStyleIndex(), getComment());
    }

    @Override
    public String toString() {
        return "CellPropertiesImpl{" +
                "styleIndex=" + ((styleIndex == null) ? "null" : '"' + styleIndex + '"') +
                ", comment=" + ((comment == null) ? "null" : "\"" + comment + "\"") +
                '}';
    }
}
