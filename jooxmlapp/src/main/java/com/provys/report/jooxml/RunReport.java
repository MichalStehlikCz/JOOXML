package com.provys.report.jooxml;

import com.provys.report.jooxml.datasource.DataSourceFactory;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.report.ReportFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
class RunReport implements Runnable {

    private File template;
    private File bodyFile;
    private File paramFile;
    private File targetFile;
    private final RepExecutor executor;
    private final ReportFactory reportFactory;
    private final DataSourceFactory dataSourceFactory;

    @Inject
    RunReport(RepExecutor executor, ReportFactory reportFactory, DataSourceFactory dataSourceFactory) {
        this.executor = Objects.requireNonNull(executor);
        this.reportFactory = Objects.requireNonNull(reportFactory);
        this.dataSourceFactory = Objects.requireNonNull(dataSourceFactory);
    }

    RunReport setTemplate(File template) {
        this.template = template;
        return this;
    }

    RunReport setBodyFile(File bodyFile) {
        this.bodyFile = bodyFile;
        return this;
    }

    RunReport setParamFile(File paramFile) {
        this.paramFile = paramFile;
        return this;
    }

    RunReport setTargetFile(File targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    private static void addLoggerShutdownHook() {
        Logger logger = LogManager.getRootLogger();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down - closing application");
            if( LogManager.getContext() instanceof LoggerContext) {
                logger.debug("Shutting down log4j2");
                Configurator.shutdown((LoggerContext)LogManager.getContext());
            } else
                logger.warn("Unable to shutdown log4j2");
        }));
    }

    @Override
    public void run() {
        addLoggerShutdownHook();
        Report report = reportFactory.build(dataSourceFactory.getRootDataSource(), bodyFile, template);
        executor.setReport(report).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                run();
    }
}
