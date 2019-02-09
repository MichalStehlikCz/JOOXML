package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.Objects;

@Singleton
public class ReportFactory {

    @Nonnull
    private final TplWorkbookFactory tplWorkbookFactory;
    @Nonnull
    private final ReportBodyReader bodyReader;

    @SuppressWarnings({"CdiInjectionPointsInspection", "CdiUnproxyableBeanTypesInspection"})
    @Inject
    ReportFactory(ReportBodyReader bodyReader, TplWorkbookFactory tplWorkbookFactory) {
        this.bodyReader = Objects.requireNonNull(bodyReader);
        this.tplWorkbookFactory = Objects.requireNonNull(tplWorkbookFactory);
    }

    public Report build(Collection<ReportDataSource> dataSources, StepBuilder rootStepBuilder, File template) {
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

    public Report build(Collection<ReportDataSource> dataSources, File bodyFile, File template) {
        StepBuilder rootStepBuilder = bodyReader.read(bodyFile);
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

}