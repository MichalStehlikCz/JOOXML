package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Data context for root data source
 */
class RootDataContext extends DataContextAncestor {

    @Nullable
    private ReportContext reportContext;

    RootDataContext(ReportDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void prepare(ReportContext reportContext) {
        if (this.reportContext == null) {
            this.reportContext = Objects.requireNonNull(reportContext);
        }
    }

    @Nonnull
    @Override
    public Stream<DataRecord> execute(DataRecord master) {
        if (reportContext == null) {
            throw new IllegalStateException("Cannot retrieve data record - data cntext not prepared");
        }
        return Stream.of(new RootDataRecord(reportContext));
    }

    @Override
    public void close() throws Exception {
        reportContext = null;
    }
}
