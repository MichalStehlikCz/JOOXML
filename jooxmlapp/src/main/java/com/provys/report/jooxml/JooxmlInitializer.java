package com.provys.report.jooxml;

import com.provys.report.jooxml.datasource.DataSourceFactory;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.repexecutor.ReportDataSource;
import com.provys.report.jooxml.report.FieldBind;
import com.provys.report.jooxml.report.ReportImpl;
import com.provys.report.jooxml.report.RowCellAreaBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import picocli.CommandLine;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(description = "Evaluate JOOXML report", name="jooxml", mixinStandardHelpOptions = true, version = "0.9")
class JooxmlInitializer implements Runnable {

    @CommandLine.Option(names = {"-t", "--template"}, required = true, description = "Template xlsx file")
    private File template;

    @CommandLine.Option(names = {"-l", "--logfile"}, description = "Log file")
    private File logFile;

    @CommandLine.Option(names = {"--loglevel"}, description = "Log level", defaultValue = "ERROR")
    private Level logLevel;

    @CommandLine.Option(names = {"-p", "--paramfile"}, description = "Parameter XML file")
    private File paramFile;

    @CommandLine.Parameters(description = "Output file to be generated")
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
        builder.add(builder.newLogger("org.jboss.weld", Level.WARN).
                addAttribute("additivity", true));
        Configurator.initialize(builder.build());
    }

    @Override
    public void run() {
        configureLogger();
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        RunReport runner = container.select(RunReport.class).get();
        runner.setTemplate(template).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                run();
        System.out.println("Processing successfully finished");
    }

}
