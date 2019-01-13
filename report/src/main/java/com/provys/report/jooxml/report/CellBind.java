package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
public final class CellBind {

    private static final Logger LOG = LogManager.getLogger(CellBind.class.getName());

    private final String sourceColumn;
    private final CellCoordinates coordinates;

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param coordinates is reference to cell as used in internal representation in POI spreadsheets
     */
    public CellBind(String sourceColumn, CellCoordinates coordinates) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param address is string reference to cell in Excel format
     */
    public CellBind(String sourceColumn, String address) {
        this(sourceColumn, CellCoordinates.parse(address));
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellCoordinates getCoordinates() {
        return coordinates;
    }
}
