package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
class CellBindBuilder {

    @Nullable
    private String sourceColumn;
    @Nullable
    private CellCoordinates coordinates;

    @Nonnull
    Optional<String> getSourceColumn() {
        return Optional.ofNullable(sourceColumn);
    }

    @Nonnull
    CellBindBuilder setSourceColumn(String sourceColumn) {
        this.sourceColumn = CellBind.validateSourceColumn(sourceColumn);
        return this;
    }

    @Nonnull
    public Optional<CellCoordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }

    @Nonnull
    public CellBindBuilder setCoordinates(CellCoordinates coordinates) {
        this.coordinates = Objects.requireNonNull(coordinates);
        return this;
    }

    /**
     * @return CellBind built from this builder
     * @throws IllegalStateException if column or coordinates are not specified
     */
    @Nonnull
    public CellBind build() {
        return new CellBind(
                getSourceColumn().orElseThrow(
                        () -> new IllegalStateException("Cannot build cell bind - column not specified")),
                getCoordinates().orElseThrow(
                        () -> new IllegalStateException("Cannot build cell bind - coordinates not specified")));
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellBindBuilder{" +
                "sourceColumn=" + getSourceColumn().map((val) -> '\'' + sourceColumn + '\'').orElse("null") +
                ", coordinates=" + getCoordinates().map(Object::toString).orElse("null") +
                '}';
    }
}
