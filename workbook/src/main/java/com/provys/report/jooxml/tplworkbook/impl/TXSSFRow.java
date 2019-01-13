package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.repexecutor.RowProperties;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.tplworkbook.TplRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class TXSSFRow implements TplRow {

    private final int rowIndex;
    private final RowProperties properties;
    private final SortedMap<Integer, TplCell> cells;

    private static RowProperties initRowProperties(Row row) {
        // problem is that underlying XSSFRow does not give access to explicitly set row height - it returns default
        // sheet row height if no row height is set on row level. To circumvent this behaviour, we will discard row
        // height if it is the same as default sheet row height.
        // If in future we decide to parse sheet ourselves, we can simplify this construct and just return explicitly
        // set height...
        float heightInPoints = (row.getHeightInPoints() == row.getSheet().getDefaultRowHeightInPoints())
                ? -1 : row.getHeightInPoints();
        int styleIndex = (row.getRowStyle() == null) ? -1 : row.getRowStyle().getIndex();
        return RowProperties.get(heightInPoints, row.getZeroHeight(), styleIndex);
    }

    private SortedMap<Integer, TplCell> initCells(Row row) {
        var result = new TreeMap<Integer, TplCell>();
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                result.put(cell.getColumnIndex(), new TXSSFCell(this, cell));
            }
        }
        return result;
    }

    TXSSFRow(Row row) {
        this.rowIndex = row.getRowNum();
        this.properties = initRowProperties(row);
        this.cells = initCells(row);
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public Optional<TplCell> getCell(int i) {
        return Optional.ofNullable(cells.get(i));
    }

    @Override
    public @NotNull Iterator<TplCell> iterator() {
        return cells.values().iterator();
    }

    @Override
    public Collection<TplCell> getCells() {
        return Collections.unmodifiableCollection(cells.values());
    }

    @Override
    public RowProperties getProperties() {
        return properties;
    }
}
