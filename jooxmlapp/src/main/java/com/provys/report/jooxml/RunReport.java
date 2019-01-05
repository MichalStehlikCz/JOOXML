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

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
class RunReport implements Runnable {

    private File template;
    private File bodyFile;
    private File paramFile;
    private File targetFile;
    @Inject
    private RepExecutor executor;
    @Inject
    private ReportFactory reportFactory;

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
        List<ReportDataSource> dataSources = new ArrayList<>(1);
        dataSources.add(DataSourceFactory.getRootDataSource());
        Report report = reportFactory.build(dataSources, bodyFile, template);
        executor.setReport(report).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                run();
    }
}
