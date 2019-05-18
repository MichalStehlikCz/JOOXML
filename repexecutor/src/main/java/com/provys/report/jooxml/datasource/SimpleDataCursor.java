package com.provys.report.jooxml.datasource;

import java.util.stream.Stream;

/**
 * Simple cursor implementation, that just wraps stream and does not return any data.
 */
public class SimpleDataCursor extends DataCursorAncestor<DataContextAncestor> {

    private final Stream<DataRecord> data;

    SimpleDataCursor(DataContextAncestor dataContext, Stream<DataRecord> data) {
        super(dataContext);
        this.data = data;
    }

    @Override
    public Stream<DataRecord> getData() {
        return data;
    }

    @Override
    public void close() {
        super.close();
        data.close();
    }
}
