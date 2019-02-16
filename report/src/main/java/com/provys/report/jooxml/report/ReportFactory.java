package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataSourceReader;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.Objects;

@ApplicationScoped
public class ReportFactory {

    @Nonnull
    private final TplWorkbookFactory tplWorkbookFactory;
    @Nonnull
    private final DataSourceReader dataSourceReader;
    @Nonnull
    private final ReportBodyReader bodyReader;

    @SuppressWarnings({"CdiInjectionPointsInspection", "CdiUnproxyableBeanTypesInspection"})
    @Inject
    ReportFactory(DataSourceReader dataSourceReader, ReportBodyReader bodyReader,
                  TplWorkbookFactory tplWorkbookFactory) {
        this.dataSourceReader = Objects.requireNonNull(dataSourceReader);
        this.bodyReader = Objects.requireNonNull(bodyReader);
        this.tplWorkbookFactory = Objects.requireNonNull(tplWorkbookFactory);
    }

    public Report build(ReportDataSource rootDataSource, StepBuilder rootStepBuilder, File template) {
        return new ReportImpl(rootDataSource, rootStepBuilder, template, tplWorkbookFactory);
    }

    public Report build(ReportDataSource rootDataSource, File bodyFile, File template) {
        StepBuilder rootStepBuilder = bodyReader.read(bodyFile);
        return new ReportImpl(rootDataSource, rootStepBuilder, template, tplWorkbookFactory);
    }

    public Report build(File dataSourceFile, File bodyFile, File template) {
        var rootDataSource = dataSourceReader.read(dataSourceFile);
        StepBuilder rootStepBuilder = bodyReader.read(bodyFile);
        return new ReportImpl(rootDataSource, rootStepBuilder, template, tplWorkbookFactory);
    }

}