package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.WorkbookFactory;

import java.util.Objects;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
public final class FieldBind {

    private final String sourceColumn;
    private final CellCoordinates coordinates;

    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param coordinates is reference to cell as used in internal representation in POI spreadsheets
     */
    public FieldBind(String sourceColumn, CellCoordinates coordinates) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param address is string reference to cell in Excel format
     */
    public FieldBind(String sourceColumn, String address) {
        this(sourceColumn, WorkbookFactory.parseCellCoordinates(address));
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellCoordinates getCoordinates() {
        return coordinates;
    }
}
