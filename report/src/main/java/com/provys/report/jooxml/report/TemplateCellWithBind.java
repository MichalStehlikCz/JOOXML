package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell from template workbook. Has coordinates relative to its parent region and holds reference to
 * bind column if one exists for the cell.
 */
class TemplateCellWithBind implements AreaCell {

    private final int columnIndex; // index of column within row, zero based; in theory, index could be taken from
                                   // template cell as long as we do not have column regions, but...
    @Nonnull
    private final TplCell cell;
    @Nullable
    private final String bindColumn;

    /**
     * Create new region cell with characteristics based on supplied template cell, that might be populated from bind
     * column if one is supplied
     *
     * @param columnIndex is column index relative to region (thus potentially different from supplied template cell's
     *                   index)
     * @param cell is template cell this cell should take formatting and other properties from
     * @param bindColumn is name of column from underlying dataset, null if value from template should be used
     */
    TemplateCellWithBind(int columnIndex, TplCell cell, @Nullable String bindColumn) {
        this.columnIndex = columnIndex;
        this.cell = Objects.requireNonNull(cell);
        if ((cell.getCellType() == CellType.FORMULA) && (bindColumn != null)) {
            throw new IllegalArgumentException("Value binding to formula cell is not allowed");
        }
        if ((cell.getCellType() == CellType.ERROR) && (bindColumn != null)) {
            throw new IllegalArgumentException("Value binding to error cell is not allowed");
        }
        this.bindColumn = bindColumn;
    }

    @Override
    public int getColIndex() {
        return columnIndex;
    }

    @Nonnull
    @Override
    public CellType getCellType() {
        return cell.getCellType();
    }

    @Nonnull
    @Override
    public Optional<CellValue> getCellValue() {
        return Optional.of(cell.getCellValue());
    }

    @Nonnull
    @Override
    public CellValue getEffectiveValue(DataRecord data) {
        return (bindColumn != null) ? data.getValue(bindColumn, getCellType()) : cell.getCellValue();
    }

    @Nonnull
    @Override
    public Optional<CellProperties> getProperties() {
        return cell.getCellProperties();
    }

    @Nonnull
    @Override
    public Optional<String> getBindColumn() {
        return Optional.ofNullable(bindColumn);
    }
}
