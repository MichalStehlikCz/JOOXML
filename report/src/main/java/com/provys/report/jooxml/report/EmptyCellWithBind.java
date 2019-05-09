package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;
import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import java.util.*;

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
    public CellValue getEffectiveValue(DataRecord data, ExecRegionContext execRegionContext,
                                       CellPathReplacer cellPathReplacer) {
        // empty cell cannot be formula, so we do not have to bother with replacing cell value
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
    public Map<String, AreaCellPath> getReferenceMap() {
        return Collections.emptyMap();
    }
}
