package com.provys.jooxml.template;

import com.provys.jooxml.repexecutor.RowProperties;
import com.provys.jooxml.report.TemplateCell;
import com.provys.jooxml.report.TemplateRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.*;

public class TXSSFRow implements TemplateRow {

    private final int rowNum;
    private final RowProperties rowProperties;
    private final SortedMap<Integer, TemplateCell> cells;

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

    private static SortedMap<Integer, TemplateCell> initCells(Row row) {
        SortedMap<Integer, TemplateCell> result = new TreeMap();
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                result.put(cell.getColumnIndex(), new TXSSFCell(cell));
            }
        }
        return result;
    }

    public TXSSFRow(Row row) {
        this.rowNum = row.getRowNum();
        this.rowProperties = initRowProperties(row);
        this.cells = initCells(row);
    }

    @Override
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public RowProperties getRowProperties() {
        return rowProperties;
    }

    @Override
    public Optional<TemplateCell> getCell(int i) {
        return Optional.ofNullable(cells.get(i));
    }

    @Override
    public Iterator<TemplateCell> iterator() {
        return cells.values().iterator();
    }

    @Override
    public Collection<TemplateCell> getCells() {
        return Collections.unmodifiableCollection(cells.values());
    }
}
