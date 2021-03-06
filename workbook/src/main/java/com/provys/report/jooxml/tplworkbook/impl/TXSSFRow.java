package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.workbook.RowProperties;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.workbook.impl.RowPropertiesInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javax.annotation.Nonnull;

import java.util.*;

class TXSSFRow implements TplRow {

    @Nonnull
    private final TXSSFSheet sheet;
    private final int rowIndex;
    @Nonnull
    private final RowProperties properties;
    @Nonnull
    private final SortedMap<Integer, TplCell> cells;

    @Nonnull
    private static RowProperties initRowProperties(Row row) {
        // problem is that underlying XSSFRow does not give access to explicitly set row height - it returns default
        // sheet row height if no row height is set on row level. To circumvent this behaviour, we will discard row
        // height if it is the same as default sheet row height.
        // If in future we decide to parse sheet ourselves, we can simplify this construct and just return explicitly
        // set height...
        Float heightInPoints = (row.getHeightInPoints() == row.getSheet().getDefaultRowHeightInPoints())
                ? null : row.getHeightInPoints();
        Short styleIndex = (row.getRowStyle() == null) ? null : row.getRowStyle().getIndex();
        return RowPropertiesInt.of(heightInPoints, row.getZeroHeight(), styleIndex);
    }

    TXSSFRow(TXSSFSheet sheet, Row row) {
        this.sheet = Objects.requireNonNull(sheet);
        this.rowIndex = row.getRowNum();
        this.properties = initRowProperties(row);
        this.cells = initCells(row);
    }

    @Nonnull
    private SortedMap<Integer, TplCell> initCells(Row row) {
        var result = new TreeMap<Integer, TplCell>();
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                result.put(cell.getColumnIndex(), new TXSSFCell(this, cell));
            }
        }
        return result;
    }

    /**
     * @return value of field sheet
     */
    TXSSFSheet getSheet() {
        return sheet;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    @Nonnull
    public Optional<TplCell> getCell(int i) {
        return Optional.ofNullable(cells.get(i));
    }

    @Override
    @Nonnull
    public Iterator<TplCell> iterator() {
        return cells.values().iterator();
    }

    @Override
    @Nonnull
    public Collection<TplCell> getCells() {
        return Collections.unmodifiableCollection(cells.values());
    }

    @Override
    @Nonnull
    public RowProperties getProperties() {
        return properties;
    }
}
