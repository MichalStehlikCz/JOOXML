package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.tplworkbook.TplSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.annotation.Nonnull;
import java.util.*;

class TXSSFSheet implements TplSheet {

    @Nonnull
    private final TXSSFWorkbook workbook;
    @Nonnull
    private final SortedMap<Integer, TplRow> rows;

    TXSSFSheet(TXSSFWorkbook workbook, Sheet sheet) {
        this.workbook = Objects.requireNonNull(workbook);
        this.rows = initRows(sheet);
    }

    @Nonnull
    private SortedMap<Integer, TplRow> initRows(Sheet sheet) {
        var result = new TreeMap<Integer, TplRow>();
        for (Row row : sheet) {
            result.put(row.getRowNum(), new TXSSFRow(this, row));
        }
        return result;
    }

    /**
     * @return value of field workbook
     */
    @Nonnull
    public TXSSFWorkbook getWorkbook() {
        return workbook;
    }

    @Override
    @Nonnull
    public Optional<TplRow> getRow(int i) {
        return Optional.ofNullable(rows.get(i));
    }

    @Override
    @Nonnull
    public Iterator<TplRow> iterator() {
        return rows.values().iterator();
    }

    @Override
    @Nonnull
    public Collection<TplRow> getRows(int firstRow, int lastRow) {
        return Collections.unmodifiableCollection(rows.subMap(firstRow
                , (lastRow == Integer.MAX_VALUE) ? lastRow : lastRow + 1).values());
    }
}
