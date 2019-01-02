package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell from template workbook. Has coordinates relative to its parent region and holds reference to
 * bind column if one exists for the cell.
 */
class TemplateCellWithBind implements AreaCell {

    private final int columnIndex; // index of column within row, zero based; in theory, index could be taken from
                                   // template cell as long as we do not have column regions, but...
    private final TplCell cell;
    private final Optional<String> bindColumn;

    /**
     * Create new region cell with characteristics based on supplied template cell, that might be populated from bind
     * column if one is supplied
     *
     * @param columnIndex
     * @param cell
     * @param bindColumn
     */
    TemplateCellWithBind(int columnIndex, TplCell cell, Optional<String> bindColumn) {
        this.columnIndex = columnIndex;
        this.cell = Objects.requireNonNull(cell);
        if ((cell.getCellType() == CellType.FORMULA) && (bindColumn.isPresent())) {
            throw new IllegalArgumentException("Value binding to formula cell is not allowed");
        }
        if ((cell.getCellType() == CellType.ERROR) && (bindColumn.isPresent())) {
            throw new IllegalArgumentException("Value binding to error cell is not allowed");
        }
        this.bindColumn = Objects.requireNonNull(bindColumn);
    }

    @Override
    public int getColIndex() {
        return cell.getColIndex();
    }

    @Override
    public CellType getCellType() {
        return cell.getCellType();
    }

    @Override
    public Optional<CellValue> getCellValue() {
        return Optional.of(cell.getCellValue());
    }

    @Override
    public CellValue getEffectiveValue(DataRecord data) {
        return bindColumn.map((column) -> data.getValue(column, getCellType())).orElse(cell.getCellValue());
    }

    @Override
    public Optional<CellProperties> getProperties() {
        return cell.getCellProperties();
    }

    @Override
    public Optional<String> getBindColumn() {
        return bindColumn;
    }
}
