package com.provys.report.jooxml;

import com.provys.report.jooxml.datasource.DataSourceFactory;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.repexecutor.ReportDataSource;
import com.provys.report.jooxml.report.FieldBind;
import com.provys.report.jooxml.report.ReportImpl;
import com.provys.report.jooxml.report.RowCellAreaBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import picocli.CommandLine.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Command(description = "Evaluate JOOXML report", name="jooxml", mixinStandardHelpOptions = true, version = "0.9")
class RunReport implements Runnable {

    @Option(names = {"-t", "--template"}, required = true, description = "Template xlsx file")
    private File template;

    @Option(names = {"-l", "--logfile"}, description = "Log file")
    private File logFile;

    @Option(names = {"--loglevel"}, description = "Log level", defaultValue = "ERROR")
    private Level logLevel;

    @Option(names = {"-p", "--paramfile"}, description = "Parameter XML file")
    private File paramFile;

    @Parameters(description = "Output file to be generated")
    private File targetFile;

    /**
     * Configure logger based on command line arguments (logfile and loglevel)
     */
    private void configureLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setConfigurationName("RootLogger");
        if (logFile != null) {
            AppenderComponentBuilder appenderBuilder = builder.newAppender("Log", "File").
                    addAttribute("fileName", logFile.getPath());
            builder.add(appenderBuilder);
        } else {
            AppenderComponentBuilder appenderBuilder = builder.newAppender("Log", "Console")
                    .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
            builder.add(appenderBuilder);
        }
        builder.add(builder.newRootLogger(logLevel).
                add(builder.newAppenderRef("Log")).
                addAttribute("additivity", true));
        Configurator.initialize(builder.build());
    }

    @Override
    public void run() {
        configureLogger();
        List<ReportDataSource> dataSources = new ArrayList<>(1);
        dataSources.add(DataSourceFactory.getRootDataSource());
        RowCellAreaBuilder rootStepBuilder = new RowCellAreaBuilder().setNameNm("ROOT").setFirstRow(0)
                .setLastRow(Integer.MAX_VALUE).setTopLevel(true)
                .addFieldBind(new FieldBind("REPORTNAME", "A1"));
        Report report = new ReportImpl(dataSources, rootStepBuilder, template);
        RepExecutor executor = new RepExecutor(report, targetFile, paramFile);
        executor.Run();
        System.out.println("Processing successfully finished");
    }

}
