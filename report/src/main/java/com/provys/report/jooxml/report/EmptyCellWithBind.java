package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell created because of binding. Cell was not present in template sheet, but there is data binding
 * for this cell in region description, thus we consider this cell to be String cell without any special formatting
 */
class EmptyCellWithBind implements AreaCell {

    private final int columnIndex;
    @Nonnull
    private final String bindColumn;

    /**
     * Constructor used when bind points to empty (non-existent) cell.
     *
     * @param columnIndex is column coordinate of new cell
     * @param bindColumn is name of bind column to supply value
     */
    EmptyCellWithBind(int columnIndex, String bindColumn) {
        this.columnIndex = columnIndex;
        this.bindColumn = Objects.requireNonNull(bindColumn);
    }

    @Override
    public int getColIndex() {
        return columnIndex;
    }

    @Nonnull
    @Override
    public CellType getCellType() {
        return CellType.BLANK;
    }

    @Nonnull
    @Override
    public Optional<CellValue> getCellValue() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public CellValue getEffectiveValue(DataRecord data) {
        return data.getCellValue(bindColumn);
    }

    @Nonnull
    @Override
    public Optional<CellProperties> getProperties() {
        return Optional.empty();
    }

    /**
     * Returns Optional to fulfill interface contract, even though bind is always present for this class
     *
     * @return source field for cell's data binding
     */
    @Nonnull
    @Override
    public Optional<String> getBindColumn() {
        return Optional.of(bindColumn);
    }

    @Nonnull
    @Override
    public Collection<CellReference> getCellReferences() {
        return Collections.emptyList();
    }
}
