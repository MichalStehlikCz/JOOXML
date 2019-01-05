package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.RootDataRecord;
import com.provys.report.jooxml.repworkbook.RepWorkbook;
import com.provys.report.jooxml.repworkbook.RepWorkbookFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private Report report;
    private File targetFile;
    private List<Parameter> parameters = new ArrayList<>(0);
    @Inject
    private RepWorkbookFactory repWorkBookFactory;

    public Report getReport() {
        return report;
    }

    public RepExecutor setReport(Report report) {
        this.report = report;
        return this;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public RepExecutor setTargetFile(File targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public RepExecutor setParameters(List<Parameter> parameters) {
        this.parameters = new ArrayList<>(parameters);
        return this;
    }

    public RepExecutor setParamFile(File paramFile) {
        try (ParameterReader reader = new ParameterReader(paramFile)) {
            setParameters(reader.getParameters());
        } catch (IOException e) {
            LOG.error("Failed to read parameters from file {} {}", paramFile, e);
            throw new RuntimeException("Failed to read parameters from file " + paramFile, e);
        }
        return this;
    }

    public RepWorkbookFactory getRepWorkBookFactory() {
        return repWorkBookFactory;
    }

    public void setRepWorkBookFactory(RepWorkbookFactory repWorkBookFactory) {
        this.repWorkBookFactory = repWorkBookFactory;
    }

    private RepWorkbook readWorkbook() {
        try {
            LOG.info("ReadWorkbook: Read template workbook from file {}", getReport().getTemplate());
            return getRepWorkBookFactory().get(getReport().getTemplate());
        } catch (IOException ex) {
            LOG.error("ReadWorkbook: IO error reading workbook {} {}", getReport().getTemplate(), ex);
            throw new RuntimeException("IO error reading workbook", ex);
        }
    }

    private void writeWorkbook(RepWorkbook workbook) {
        LOG.info("WriteWorkbook: Write workbook to file {}", getTargetFile());
        try (OutputStream stream = Files.newOutputStream(getTargetFile().toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            workbook.write(stream);
        } catch (IOException ex) {
            LOG.error("WriteWorkbook: IO error writing workbook {} {}", getTargetFile(), ex);
            throw new RuntimeException("IO error writing workbook", ex);
        }
    }

    public void run() {
        try (ReportContext reportContext = new ReportContext(getParameters())) {
            try (RepWorkbook workbook = readWorkbook()) {
                reportContext.open(workbook);
                StepContext stepContext = new StepContext(reportContext, new RootDataRecord(reportContext)
                        , new ContextCoordinates(workbook.getSheet(), 0, 0));
                Stream<StepProcessor> pipeline
                        = Stream.of(getReport().getRootStep().getProcessorSupplier().apply(stepContext));
                getReport().getRootStep().addStepProcessing(pipeline).forEachOrdered(StepProcessor::execute);
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
