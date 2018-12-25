package com.provys.jooxml.report;

import org.apache.poi.ss.util.CellReference;

import java.util.Objects;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
public final class FieldBind {

    private final String sourceColumn;
    private final CellReference cellReference;

    /**
     * Verifikuje, ze se jedna o referenci pouzitelnou pro binding - nesmi mit zadany sheet a musi mit relativni pozice
     *
     * @param cellReference is reference being validated
     */
    private static CellReference validateCellReference(CellReference cellReference) {
        Objects.requireNonNull(cellReference);
        if (cellReference.getSheetName() != null) {
            throw new IllegalArgumentException("Cannot specify sheet name in binding " + cellReference.getSheetName());
        }
        if (cellReference.isColAbsolute()) {
            throw new IllegalArgumentException("Cannot specify absolute ($) column position");
        }
        if (cellReference.isRowAbsolute()) {
            throw new IllegalArgumentException("Cannot specify absolute ($) row position");
        }
        if (cellReference.getRow() == -1) {
            throw new IllegalArgumentException("Row ot supplied in data binding");
        }
        if (cellReference.getCol() == -1) {
            throw new IllegalArgumentException("Column not supplied in data binding");
        }
        return cellReference;
    }
    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param cellReference is reference to cell as used in internal representation in POI spreadsheets
     */
    public FieldBind(String sourceColumn, CellReference cellReference) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.cellReference = Objects.requireNonNull(validateCellReference(cellReference));
    }

    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param cellAddress is cell name (e.g. A5)
     */
    public FieldBind(String sourceColumn, String cellAddress) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.cellReference = validateCellReference(new CellReference(Objects.requireNonNull(cellAddress)));
    }

    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param row is row number
     * @param column is column number
     */
    public FieldBind(String sourceColumn, int row, int column) {
        this.sourceColumn = sourceColumn;
        this.cellReference = new CellReference(row, column);
    }

    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param sheetName is name of sheet this rule is targeted at
     * @param row is row number
     * @param column is column number
     */
    public FieldBind(String sourceColumn, String sheetName, int row, int column) {
        this.sourceColumn = sourceColumn;
        this.cellReference = new CellReference(sheetName, row, column, false, false);
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellReference getCellReference() {
        return cellReference;
    }
}
