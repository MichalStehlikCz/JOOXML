package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.tplworkbook.TplSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class TXSSFSheet implements TplSheet {

    private final SortedMap<Integer, TplRow> rows;

    private static SortedMap<Integer, TplRow> initRows(Sheet sheet) {
        var result = new TreeMap<Integer, TplRow>();
        for (Row row : sheet) {
            result.put(row.getRowNum(), new TXSSFRow(row));
        }
        return result;
    }

    TXSSFSheet(Sheet sheet) {
        this.rows = initRows(sheet);
    }

    @Override
    public Optional<TplRow> getRow(int i) {
        return Optional.ofNullable(rows.get(i));
    }

    @Override
    public Iterator<TplRow> iterator() {
        return rows.values().iterator();
    }

    @Override
    public Collection<TplRow> getRows(int firstRow, int lastRow) {
        return Collections.unmodifiableCollection(rows.subMap(firstRow
                , (lastRow == Integer.MAX_VALUE) ? lastRow : lastRow + 1).values());
    }
}
