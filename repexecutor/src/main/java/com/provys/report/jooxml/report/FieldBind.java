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

    public String getSourceColumn() {
        return sourceColumn;
    }

    public CellAddress getCellAddress() {
        return cellAddress;
    }
}
