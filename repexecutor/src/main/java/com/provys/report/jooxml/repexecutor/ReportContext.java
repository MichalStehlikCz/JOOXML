package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repworkbook.RepWorkbook;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Class holds context, needed for execution of report - report worksheet, connections, data contexts ...
 */
public class ReportContext implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());

    private boolean isOpen = false;
    @Nonnull
    private final CellValueFactory cellValueFactory;
    @Nonnull
    private final Map<ReportDataSource, DataContext> dataContexts = new ConcurrentHashMap<>(3);
    @Nonnull
    private final Map<String, Connection> connections = new HashMap<>(3);
    @Nonnull
    private final Map<String, Optional<String>> parameters;
    @Nullable
    private RepWorkbook workbook;

    /**
     * Creates report context for execution of report with given parameters.
     *
     * @param parameters parameters report is to be executed with
     */
    ReportContext(@Nullable Collection<Parameter> parameters, CellValueFactory cellValueFactory) {
        if (parameters == null) {
            this.parameters = new HashMap<>(0);
        } else {
            this.parameters = parameters.stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
        }
        this.cellValueFactory = cellValueFactory;
    }

    /**
     * Starts execution of report. Registers workbook and prepares all data contexts.
     * @param workbook is target report workbook
     */
    void open(RepWorkbook workbook) {
        isOpen = true;
        this.workbook = Objects.requireNonNull(workbook);
    }

    @Override
    public void close() throws Exception {
        if (isOpen) {
            isOpen = false;
            // close all data contexts
            for (DataContext dataContext : dataContexts.values()) {
                dataContext.close();
            }
            dataContexts.clear();
            // close all used connections / return them to pool
            for (Connection connection : connections.values()) {
                connection.close();
            }
            connections.clear();
            // and close workbook
            if (workbook != null) {
                workbook.close();
            }
            workbook = null;
        }
    }

    /**
     * @return value of field cellValueFactory
     */
    @Nonnull
    public CellValueFactory getCellValueFactory() {
        return cellValueFactory;
    }

    /**
     * Retrieve data context for given data source.
     * It tries to find it in cache. If not found, retrieves data context from data source and registers it in cache
     *
     * @param dataSource is report data source that defines data access
     * @return data context for this report execution and data source, used to retrieve data
     * @throws IllegalStateException if report context is not opened
     */
    @Nonnull
    public DataContext getDataContext(ReportDataSource dataSource) {
        DataContext dataContext = dataContexts.computeIfAbsent(Objects.requireNonNull(dataSource),
                ReportDataSource::getDataContext);
        dataContext.prepare(this);
        return dataContext;
    }

    /**
     * @return value of specified parameter
     */
    public Optional<String> getParameterValue(String name) {
        Optional<String> value = parameters.get(name);
        if (value.isEmpty()) {
            LOG.warn("ReportContext: Trying to access non-existent parameter " + name);
        }
        return value;
    }

    /**
     * @return target workbook
     */
    @Nonnull
    public RepWorkbook getWorkbook() {
        if (workbook == null) {
            throw new IllegalStateException("Method getWorkbook should only be accessed when report context is opened");
        }
        return workbook;
    }

}
