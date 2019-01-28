package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@Singleton
public class ReportFactory {

    @Nonnull
    private final TplWorkbookFactory tplWorkbookFactory;
    @Nonnull
    private final ReportBodyReader bodyReader;

    @SuppressWarnings("CdiInjectionPointsInspection")
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