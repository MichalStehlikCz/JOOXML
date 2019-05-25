package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract class StreamDataCursorAncestor<T extends DataContextAncestor> extends DataCursorAncestor<T>
        implements Iterator<DataRecord> {

    private long rowNumber = 0;

    StreamDataCursorAncestor(T dataContext) {
        super(dataContext);
    }

    @Override
    public Stream<DataRecord> getData() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED), false);
    }

    @Nonnull
    abstract DataRecord getNext(long rowNumber);

    @Override
    public DataRecord next() {
        return getNext(rowNumber++);
    }
}
