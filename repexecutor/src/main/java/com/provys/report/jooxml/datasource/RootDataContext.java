package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Data context for root data source.
 * Keeps reference to report context and passes it to associated root data record
 */
class RootDataContext extends DataContextAncestor<RootDataSource> {

    RootDataContext(RootDataSource dataSource, ReportContext reportContext) {
        super(dataSource, reportContext);
    }

    @Nonnull
    @Override
    public DataCursor execute(DataRecord master) {
        var cursor = new SimpleDataCursor(this, Stream.of(new RootDataRecord(getReportContext())));

        return cursor;
    }
}
