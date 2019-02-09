package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Common ancestor for DataContext classes. Holds associated {@code ReportDataSource} and gives access to it
 * @param <T> is type of data source given context can reference
 */
abstract class DataContextAncestor<T extends ReportDataSource> implements DataContext {

    @Nonnull
    private final T dataSource;

    DataContextAncestor(T dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Nonnull
    @Override
    public T getDataSource() {
        return dataSource;
    }
}
