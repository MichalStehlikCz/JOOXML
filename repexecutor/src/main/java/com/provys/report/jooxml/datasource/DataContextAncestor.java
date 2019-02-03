package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class DataContextAncestor implements DataContext {

    @Nonnull
    private final ReportDataSource dataSource;

    DataContextAncestor(ReportDataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Nonnull
    @Override
    public ReportDataSource getDataSource() {
        return dataSource;
    }
}
