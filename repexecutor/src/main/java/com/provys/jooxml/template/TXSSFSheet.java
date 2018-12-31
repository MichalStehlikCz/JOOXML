package com.provys.jooxml.template;

import com.provys.jooxml.report.TemplateRow;
import com.provys.jooxml.report.TemplateSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class TXSSFSheet implements TemplateSheet {
    private final Sheet sheet;
    private final SortedMap<Integer, TemplateRow> rows;

    private static SortedMap<Integer, TemplateRow> initRows(Sheet sheet) {
        SortedMap<Integer, TemplateRow> result = new TreeMap();
        for (Row row : sheet) {
            result.put(row.getRowNum(), new TXSSFRow(row));
        }
        return result;
    }

    public TXSSFSheet(Sheet sheet) {
        this.sheet = sheet;
        this.rows = initRows(sheet);
    }

    @Override
    public Optional<TemplateRow> getRow(int i) {
        return Optional.ofNullable(rows.get(i));
    }

    @Override
    public Iterator<TemplateRow> iterator() {
        return rows.values().iterator();
    }

    @Override
    public Collection<TemplateRow> getRows(int firstRow, int lastRow) {
        return Collections.unmodifiableCollection(rows.subMap(firstRow
                , (lastRow == Integer.MAX_VALUE) ? lastRow : lastRow + 1).values());
    }
}
