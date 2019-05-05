package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Cursor;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SelectDataCursor extends DataCursorAncestor implements Iterator<DataRecord> {

    private final Cursor<?> cursor;
    private int rowNumber = 0;

    SelectDataCursor(SelectDataContext dataContext, Cursor<?> cursor) {
        super(dataContext);
        this.cursor = cursor;
    }

    @Override
    public boolean hasNext() {
        return cursor.hasNext();
    }

    @Override
    public DataRecord next() {
        if (!cursor.hasNext()) {
            throw new NoSuchElementException();
        }
        return new SelectDataRecord(getDataContext().getReportContext(), rowNumber++, cursor.fetchNext());
    }

    @Override
    public Stream<DataRecord> getData() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED), false);
    }

    @Override
    public void close() {
        super.close();
        cursor.close();
    }
}
