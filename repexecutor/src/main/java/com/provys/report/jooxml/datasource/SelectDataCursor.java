package com.provys.report.jooxml.datasource;

import org.jooq.Cursor;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

class SelectDataCursor extends StreamDataCursorAncestor<SelectDataContext> {

    private final Cursor<?> cursor;

    SelectDataCursor(SelectDataContext dataContext, Cursor<?> cursor) {
        super(dataContext);
        this.cursor = cursor;
    }

    @Override
    public boolean hasNext() {
        return cursor.hasNext();
    }

    @Nonnull
    @Override
    DataRecord getNext(int rowNumber) {
        if (!cursor.hasNext()) {
            throw new NoSuchElementException();
        }
        return new SelectDataRecord(getDataContext().getReportContext(), rowNumber, cursor.fetchNext());
    }

    @Override
    public void close() {
        super.close();
        cursor.close();
    }
}
