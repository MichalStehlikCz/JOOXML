package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents cell from template workbook. Has coordinates relative to its parent region and holds reference to
 * bind column if one exists for the cell.
 */
class TemplateCellWithBind implements AreaCell {

    private final int columnIndex; // index of column within row, zero based; in theory, index could be taken from
                                   // template cell as long as we do not have column regions, but...
    @Nonnull
    private final TplCell cell;
    @Nonnull
    private final Map<String, AreaCellPath> referenceMap;
    @Nullable
    private final String bindColumn;

    private static Map<String, AreaCellPath> calcReferenceMap(Map<String, CellReference> tplReferenceMap,
                                                              StepBuilder stepBuilder) {
        var result = new HashMap<String, AreaCellPath>();
        var rootStepBuilder = stepBuilder.getRoot();
        for (var entry : tplReferenceMap.entrySet()) {
            result.put(entry.getKey(), rootStepBuilder.getPath(stepBuilder, entry.getValue()).orElse(null));
        }
        return result;
    }
    /**
     * Create new region cell with characteristics based on supplied template cell, that might be populated from bind
     * column if one is supplied
     *
     * @param columnIndex is column index relative to region (thus potentially different from supplied template cell's
     *                   index)
     * @param cell is template cell this cell should take formatting and other properties from
     * @param bindColumn is name of column from underlying dataset, null if value from template should be used
     */
    TemplateCellWithBind(int columnIndex, TplCell cell, StepBuilder stepBuilder, @Nullable String bindColumn) {
        this.columnIndex = columnIndex;
        this.cell = Objects.requireNonNull(cell);
        if ((cell.getCellType() == CellType.FORMULA) && (bindColumn != null)) {
            throw new IllegalArgumentException("Value binding to formula cell is not allowed");
        }
        if ((cell.getCellType() == CellType.ERROR) && (bindColumn != null)) {
            throw new IllegalArgumentException("Value binding to error cell is not allowed");
        }
        var tplReferenceMap = cell.getReferenceMap();
        if (tplReferenceMap.isEmpty()) {
            this.referenceMap = Collections.emptyMap();
        } else {
            this.referenceMap = calcReferenceMap(tplReferenceMap, Objects.requireNonNull(stepBuilder));
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
    public CellValue getEffectiveValue(DataRecord data, ExecRegionContext execRegionContext,
                                       CellPathReplacer cellPathReplacer) {
        var cellValue = (bindColumn != null) ? data.getCellValue(bindColumn, getCellType()) : cell.getCellValue();
        if (cellValue.getCellType() == CellType.FORMULA) {
            cellValue = cellPathReplacer.encode(cellValue, getReferenceMap(), execRegionContext);
        }
        return cellValue;
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

    @Nonnull
    @Override
    public Map<String, AreaCellPath> getReferenceMap() {
        return referenceMap;
    }
}
