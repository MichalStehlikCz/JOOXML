package com.provys.report.jooxml.repexecutor;

import com.provys.provysdb.ProvysDbContext;
import com.provys.report.jooxml.datasource.RootDataRecord;
import com.provys.report.jooxml.repworkbook.RepWorkbook;
import com.provys.report.jooxml.repworkbook.RepWorkbookFactory;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

@RequestScoped
public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    /** workbook factory used to create and edit resulting workbook */
    @Nonnull
    private final RepWorkbookFactory repWorkBookFactory;
    /** Cell value factory used to create cell values during report execution */
    @Nonnull
    private final CellValueFactory cellValueFactory;
    /**
     *  Cell path replacer replaces cell references with path expressions and then decodes them back to valid cell
     *  references in generated workbook
     */
    @Nonnull
    private final CellPathReplacer cellPathReplacer;
    @Nonnull
    private final ProvysDbContext dbContext;
    private Report report;
    private File targetFile;
    private List<Parameter> parameters = new ArrayList<>(0);
    private String dbToken;

    @SuppressWarnings({"CdiInjectionPointsInspection", "CdiUnproxyableBeanTypesInspection"})
    // implementations are supplied from libraries
    @Inject
    public RepExecutor(RepWorkbookFactory repWorkBookFactory, CellValueFactory cellValueFactory,
                       CellPathReplacer cellPathReplacer, ProvysDbContext dbContext) {
        this.repWorkBookFactory = Objects.requireNonNull(repWorkBookFactory);
        this.cellValueFactory = Objects.requireNonNull(cellValueFactory);
        this.cellPathReplacer = Objects.requireNonNull(cellPathReplacer);
        this.dbContext = Objects.requireNonNull(dbContext);
    }

    /**
     * @return value of field cellValueFactory
     */
    @Nonnull
    CellValueFactory getCellValueFactory() {
        return cellValueFactory;
    }

    /**
     * @return value of field cellPathReplacer
     */
    @Nonnull
    public CellPathReplacer getCellPathReplacer() {
        return cellPathReplacer;
    }

    @Nonnull
    RepWorkbookFactory getRepWorkBookFactory() {
        return repWorkBookFactory;
    }

    public Report getReport() {
        return report;
    }

    @Nonnull
    public RepExecutor setReport(Report report) {
        this.report = report;
        return this;
    }

    File getTargetFile() {
        return targetFile;
    }

    @Nonnull
    public RepExecutor setTargetFile(File targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    List<Parameter> getParameters() {
        return parameters;
    }

    @Nonnull
    RepExecutor setParameters(List<Parameter> parameters) {
        this.parameters = new ArrayList<>(parameters);
        return this;
    }

    @Nonnull
    public RepExecutor setParamFile(File paramFile) {
        try (ParameterReader reader = new ParameterReader(paramFile)) {
            setParameters(reader.getParameters());
        } catch (IOException e) {
            LOG.error("Failed to read parameters from file {} {}", paramFile, e);
            throw new RuntimeException("Failed to read parameters from file " + paramFile, e);
        }
        return this;
    }

    /**
     * @return value of field dbToken
     */
    @Nonnull
    public Optional<String> getDbToken() {
        return Optional.ofNullable(dbToken);
    }

    /**
     * Set value of field dbToken
     *
     * @param dbToken is new value to be set
     * @return self to enable chaining
     */
    public RepExecutor setDbToken(@Nullable String dbToken) {
        this.dbToken = dbToken;
        return this;
    }

    @Nonnull
    private RepWorkbook readWorkbook() {
        try {
            LOG.info("ReadWorkbook: Read template workbook from file {}", getReport().getTemplate());
            return getRepWorkBookFactory().get(getReport().getTemplate());
        } catch (IOException ex) {
            LOG.error("ReadWorkbook: IO error reading workbook {} {}", getReport().getTemplate(), ex);
            throw new RuntimeException("IO error reading workbook", ex);
        }
    }

    private void writeWorkbook(RepWorkbook workbook, ExecRegion execRegion) {
        LOG.info("WriteWorkbook: Write workbook to file {}", getTargetFile());
        try (OutputStream stream = Files.newOutputStream(getTargetFile().toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            workbook.write(stream, execRegion);
        } catch (IOException ex) {
            LOG.error("WriteWorkbook: IO error writing workbook {} {}", getTargetFile(), ex);
            throw new RuntimeException("IO error writing workbook", ex);
        }
    }

    public void run() {
        try (ReportContext reportContext = new ReportContext(getParameters(), getCellValueFactory(),
                getCellPathReplacer());
             RepWorkbook workbook = readWorkbook();
             DSLContext dslContext = dbContext.createDSL(dbToken)) {
            reportContext.open(workbook, dslContext);
            StepContext stepContext = new StepContext(reportContext, new RootDataRecord(reportContext)
                    , new ContextCoordinates(workbook.getSheet(), 0, 0));
            var execRegion = new ExecRegionRoot();
            Stream<StepProcessor> pipeline
                    = Stream.of(getReport().getRootStep().getProcessor(stepContext,
                    new ExecRegionContext(null, execRegion)));
            // we add necessary number of step processor expansions to pipeline
            for (int i = getReport().getRootStep().getNeededProcessorApplications(); i > 0; i--) {
                pipeline = pipeline.flatMap(StepProcessor::apply);
            }
            // and once only terminal steps remain, we execute them
            pipeline.forEachOrdered(StepProcessor::execute);
            writeWorkbook(workbook, execRegion);
        } catch (IOException ex) {
            LOG.error("Workbook: IO error running report {}", ex);
            throw new RuntimeException("IO error running report", ex);
        } catch (Exception ex) {
            LOG.error("Report Context: error running report {}", ex);
            throw new RuntimeException("Error running report", ex);
        }
    }
}

