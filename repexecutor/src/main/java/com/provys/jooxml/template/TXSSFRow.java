package com.provys.jooxml.template;

import com.provys.jooxml.report.TemplateCell;
import com.provys.jooxml.report.TemplateRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.*;

public class TXSSFRow implements TemplateRow {
    private final Row row;
    private final SortedMap<Integer, TemplateCell> cells;

    private static SortedMap<Integer, TemplateCell> initCells(Row row) {
        SortedMap<Integer, TemplateCell> result = new TreeMap();
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                result.put(row.getRowNum(), new TXSSFCell(cell));
            }
        }
        return result;
    }

    public TXSSFRow(Row row) {
        this.row = row;
        this.cells = initCells(row);
    }

    @Override
    public int getRowNum() {
        return row.getRowNum();
    }

    @Override
    public int getPhysicalNumberOfCells() {
        return cells.size();
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
