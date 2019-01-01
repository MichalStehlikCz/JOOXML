package com.provys.report.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class holds context, needed for execution of report - report worksheet, connections, data contexts ...
 */
public class ReportContext implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());

    private boolean isOpen = false;
    private final List<DataContext> dataContexts = new ArrayList<>(3);
    private final Map<String, Connection> connections = new HashMap<>(3);
    private final Map<String, Optional<String>> parameters;
    private RepWorkbook workbook;

    /**
     * Creates report context for execution of reort with given parameters.
     *
     * @param parameters
     */
    ReportContext(List<Parameter> parameters) {
        if (parameters == null) {
            this.parameters = new HashMap<>(0);
        } else {
            this.parameters = parameters.stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
        }
    }

    /**
     * Starts execution of report. Registers workbook and prepares all data contexts.
     * @param workbook
     */
    public void open(RepWorkbook workbook) {
        isOpen = true;
        this.workbook = Objects.requireNonNull(workbook);
        for (DataContext dataContext : dataContexts) {
            dataContext.prepare();
        }
    }

    @Override
    public void close() throws Exception {
        if (isOpen) {
            isOpen = false;
            // close all data contexts
            for (DataContext dataContext : dataContexts) {
                dataContext.close();
            }
            // close all used connections / return them to pool
            for (Connection connection : connections.values()) {
                connection.close();
            }
            // and close workbook
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    /**
     * @return value of specified parameter
     */
    public Optional<String> getParameterValue(String name) {
        Optional<String> value = parameters.get(name);
        if (value == null) {
            LOG.warn("ReportContext: Trying to access non-existent parameter " + name);
            value = Optional.empty();
        }
        return value;
    }

    /**
     * @return target workbook
     */
    public RepWorkbook getWorkbook() {
        return workbook;
    }
}
