package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

@Singleton
public class ReportFactory {

    private static final Logger LOG = LogManager.getLogger(StepParser.class.getName());

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private TplWorkbookFactory tplWorkbookFactory;
    @Inject
    StepParser rootStepParser;

    public Report build(Collection<ReportDataSource> dataSources, StepBuilder rootStepBuilder, File template) {
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

    public Report build(Collection<ReportDataSource> dataSources, File bodyFile, File template) {
        StepBuilder rootStepBuilder;
        try (var bodyFileStream = new FileInputStream(bodyFile)) {
            XMLStreamReader stepReader = null;
            try {
                stepReader = XMLInputFactory.newInstance().createXMLStreamReader(bodyFileStream);
                rootStepBuilder = rootStepParser.parse(null, stepReader);
            } finally {
                if (stepReader != null) {
                    stepReader.close();
                }
            }

        } catch (FileNotFoundException e) {
            LOG.error("ReadParameters: Parameter file not found {} {}", bodyFile.getName(), e);
            throw new RuntimeException("Parameter file not found", e);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML body file", e);
        } catch (IOException e) {
            throw new RuntimeException("IO exception reading body file", e);
        }
        return new ReportImpl(dataSources, rootStepBuilder, template, tplWorkbookFactory);
    }

}