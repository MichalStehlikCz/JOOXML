package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Singleton
public class ReportFactory {

    private static final Logger LOG = LogManager.getLogger(StepReader.class.getName());

    @Inject
    private TplWorkbookFactory tplWorkbookFactory;

    public Report build(Collection<ReportDataSource> dataSources, StepBuilder rootStepBuilder, File template) {
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

    public Report build(Collection<ReportDataSource> dataSources, File bodyFile, File template) {
        StepBuilder rootStepBuilder;
        try (StepReader reader = new StepReader(bodyFile)) {
            rootStepBuilder = reader.getRootStep();
        } catch (IOException e) {
            LOG.error("ReadBody: Error reading file with report body {}", bodyFile, e);
            throw new RuntimeException("ReadBody: Error reading file with report body " + bodyFile, e);
        }
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

}