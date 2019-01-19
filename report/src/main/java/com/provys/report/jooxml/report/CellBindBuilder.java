package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class CellBindBuilder {

    private static final Logger LOG = LogManager.getLogger(CellBindBuilder.class.getName());

    @Nullable
    private String sourceColumn;
    @Nullable
    private CellCoordinates coordinates;

    public Optional<String> getSourceColumn() {
        return Optional.ofNullable(sourceColumn);
    }

    public CellBindBuilder setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
        return this;
    }

    public Optional<CellCoordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }

    public CellBindBuilder setCoordinates(CellCoordinates coordinates) {
        getCoordinates().filter((val) -> !val.equals(coordinates)).
                ifPresent((val) -> LOG.warn("Duplicate CELL assignment {} > {}", val, sourceColumn));
        this.coordinates = coordinates;
        return this;
    }

    public Optional<String> getAddress() {
        return (coordinates == null) ? Optional.empty() : Optional.of(coordinates.getAddress());
    }

    public CellBindBuilder setAddress(String address) {
        setCoordinates(CellCoordinates.parse(address));
        return this;
    }

    /**
     * @return CellBind built from this builder
     * @throws IllegalStateException if column or coordinates are not specified
     */
    public CellBind build() {
        return new CellBind(
                getSourceColumn().orElseThrow(
                        () -> new IllegalStateException("Cannot build cell bind - column not specified")),
                getCoordinates().orElseThrow(
                        () -> new IllegalStateException("Cannot build cell bind - coordinates not specified")));
    }
}
