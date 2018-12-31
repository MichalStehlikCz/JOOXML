package com.provys.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.provys.jooxml.worksheet.RXSSWorkbookFactory;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private Report report;
    private File targetFile;
    private Map<String, String> parameters = new HashMap<>(1);
    final private RepWorkbookFactory repWorkBookFactory = new RXSSWorkbookFactory();

    private static Map<String, String> readParameters(InputStream paramFileStream) {
        XMLStreamReader paramReader;
        try {
            paramReader = XMLInputFactory.newInstance().createXMLStreamReader(paramFileStream);
        } catch (XMLStreamException e) {
            LOG.error("ReadParameters: Exception reading XML parameter file {}", e);
            throw new RuntimeException("Exception reading XML parameter file", e);
        }

    }

    public RepExecutor(Report report, File targetFile, Map<String, String> parameters) {
        this.report = report;
        this.targetFile = targetFile;
        this.parameters.putAll(parameters);
    }

    private RepWorkbook readWorkbook() {
        try {
            LOG.info("ReadWorkbook: Read template workbook from file {}", report.getTemplate());
            return repWorkBookFactory.get(report.getTemplate());
        } catch (IOException ex) {
            LOG.error("ReadWorkbook: IO error reading workbook {} {}", report.getTemplate(), ex);
            throw new RuntimeException("IO error reading workbook", ex);
        } catch (InvalidFormatException ex) {
            LOG.error("ReadWorkbook: Invalid format exception reading {} {}", report.getTemplate(), ex);
            throw new RuntimeException("Format error reading workbook", ex);
        }
    }

    private void writeWorkbook(RepWorkbook workbook) {
        LOG.info("WriteWorkbook: Write workbook to file {}", targetFile);
        try (OutputStream stream = Files.newOutputStream(targetFile.toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            workbook.write(stream);
        } catch (IOException ex) {
            LOG.error("WriteWorkbook: IO error writing workbook {} {}", targetFile, ex);
            throw new RuntimeException("IO error writing workbook", ex);
        }
    }

    public void Run() {
        try (ReportContext reportContext = new ReportContext(parameters)) {
            try (RepWorkbook workbook = readWorkbook()) {
                reportContext.open(workbook);
                StepContext stepContext = new StepContext(reportContext, new RootDataRecord(reportContext)
                        , new ContextCoordinates(workbook.getSheet(), 0, 0));
                Stream<StepProcessor> pipeline
                        = Stream.of(report.getRootStep().getProcessorSupplier().apply(stepContext));
                report.getRootStep().addStepProcessing(pipeline).forEachOrdered(StepProcessor::execute);
                writeWorkbook(workbook);
            } catch (IOException ex) {
                LOG.error("Workbook: IO error closing workbook {}", ex);
                throw new RuntimeException("IO error closing workbook", ex);
            }
        } catch (Exception ex) {
            LOG.error("Report Context: error closing report context {}", ex);
            throw new RuntimeException("Error closing context", ex);
        }
    }
}
