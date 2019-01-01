package com.provys.report.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.provys.report.jooxml.worksheet.RXSSWorkbookFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private Report report;
    private File targetFile;
    private List<Parameter> parameters;
    final private RepWorkbookFactory repWorkBookFactory = new RXSSWorkbookFactory();

    public RepExecutor(Report report, File targetFile, List<Parameter> parameters) {
        this.report = report;
        this.targetFile = targetFile;
        this.parameters = new ArrayList<>(parameters);
    }

    public RepExecutor(Report report, File targetFile, File paramFile) {
        this.report = Objects.requireNonNull(report);
        this.targetFile = Objects.requireNonNull(targetFile);
        if (paramFile != null) {
            try (ParameterReader reader = new ParameterReader(paramFile)) {
                this.parameters = new ArrayList<>(reader.getParameters());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.parameters = new ArrayList<>(0);
        }
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
