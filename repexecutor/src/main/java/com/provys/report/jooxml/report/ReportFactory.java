package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;

@Singleton
public class ReportFactory {

    @Inject
    private TplWorkbookFactory tplWorkbookFactory;

    public Report build(Collection<ReportDataSource> dataSources, StepBuilder rootRegionBuilder, File template) {
        return new ReportImpl(dataSources, rootRegionBuilder, template, tplWorkbookFactory);
    }

}