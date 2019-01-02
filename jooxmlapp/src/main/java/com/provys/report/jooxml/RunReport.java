package com.provys.report.jooxml;

import com.provys.report.jooxml.datasource.DataSourceFactory;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.repexecutor.ReportDataSource;
import com.provys.report.jooxml.report.FieldBind;
import com.provys.report.jooxml.report.ReportImpl;
import com.provys.report.jooxml.report.RowCellAreaBuilder;
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
    private File paramFile;
    private File targetFile;
    @Inject
    private RepExecutor executor;

    RunReport setTemplate(File template) {
        this.template = template;
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
        RowCellAreaBuilder rootStepBuilder = new RowCellAreaBuilder().setNameNm("ROOT").setFirstRow(0)
                .setLastRow(Integer.MAX_VALUE).setTopLevel(true)
                .addFieldBind(new FieldBind("REPORTNAME", "A1"));
        Report report = new ReportImpl(dataSources, rootStepBuilder, template);
        executor.setReport(report).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                run();
        System.out.println("Processing successfully finished");
    }

}
