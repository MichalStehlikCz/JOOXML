package com.provys.report.jooxml.datasource;

import java.util.stream.Stream;

public class EmptyDataCursor extends DataCursorAncestor {

    EmptyDataCursor(DataContextAncestor dataContext) {
        super(dataContext);
    }

    @Override
    public Stream<DataRecord> getData() {
        return Stream.empty();
    }
}
