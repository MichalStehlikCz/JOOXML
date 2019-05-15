package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class DataStep extends Step implements StepWithChild {
    @Nonnull
    private final ReportDataSource dataSource;
    @Nonnull
    private final ReportStep child;

    DataStep(String nameNm, ReportDataSource dataSource, ReportStep child) {
        super(nameNm);
        this.dataSource = Objects.requireNonNull(dataSource);
        this.child = Objects.requireNonNull(child);
    }

    @Nonnull
    ReportDataSource getDataSource() {
        return dataSource;
    }

    @Nonnull
    public ReportStep getChild() {
        return child;
    }

    @Override
    public int getNeededProcessorApplications() {
        return getChild().getNeededProcessorApplications() + 1;
    }
}
