package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public class CellBindBuilder {

    private static final Logger LOG = LogManager.getLogger(CellBind.class.getName());

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
}
