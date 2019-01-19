package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
final class CellBind {

    @Nonnull
    private final String sourceColumn;
    @Nonnull
    private final CellCoordinates coordinates;

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param coordinates is reference to cell as used in internal representation in POI spreadsheets
     */
    CellBind(String sourceColumn, CellCoordinates coordinates) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param address is string reference to cell in Excel format
     */
    CellBind(String sourceColumn, String address) {
        this(sourceColumn, CellCoordinates.parse(address));
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellCoordinates getCoordinates() {
        return coordinates;
    }
}
