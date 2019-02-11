package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
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
    public Stream<DataRecord> execute(DataRecord master) {
        return Stream.of(new RootDataRecord(getReportContext()));
    }
}
