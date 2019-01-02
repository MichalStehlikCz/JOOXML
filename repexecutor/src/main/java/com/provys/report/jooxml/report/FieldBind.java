package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellAddress;

import java.util.Objects;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
public final class FieldBind {

    private final String sourceColumn;
    private final CellAddress cellAddress;

    /**
     * Verifikuje, ze se jedna o referenci pouzitelnou pro binding - nesmi mit zadany sheet a musi mit relativni pozice
     *
     * @param cellAddress is reference being validated
     */
    private static CellAddress validateCellAddress(CellAddress cellAddress) {
        Objects.requireNonNull(cellAddress);
        if (cellAddress.getSheetName().isPresent()) {
            throw new IllegalArgumentException("Cannot specify sheet name in binding " + cellAddress.getSheetName().get());
        }
        return cellAddress;
    }
    /**
     * Creates FieldBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param cellAddress is reference to cell as used in internal representation in POI spreadsheets
     */
    public FieldBind(String sourceColumn, CellAddress cellAddress) {
        this.sourceColumn = Objects.requireNonNull(sourceColumn);
        this.cellAddress = validateCellAddress(cellAddress);
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
        this.cellAddress = new CellAddress(sheetName, row, column, false, false);
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellAddress getCellAddress() {
        return cellAddress;
    }
}
