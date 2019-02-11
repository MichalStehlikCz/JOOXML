package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Common ancestor for DataContext classes. Holds associated {@code ReportDataSource} and gives access to it
 * @param <T> is type of data source given context can reference
 */
abstract class DataContextAncestor<T extends ReportDataSource> implements DataContext {

    @Nonnull
    private final T dataSource;

    @Nonnull
    private final ReportContext reportContext;

    DataContextAncestor(T dataSource, ReportContext reportContext) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    @Nonnull
    @Override
    public T getDataSource() {
        return dataSource;
    }

    /**
     * @return value of field reportContext
     */
    @Nonnull
    public ReportContext getReportContext() {
        return reportContext;
    }

    @Override
    public void prepare() {
        // by default, data context does not need any preparation
    }

    @Override
    public void close() {
        // by default, data context has no resources to be closed
    }

}
