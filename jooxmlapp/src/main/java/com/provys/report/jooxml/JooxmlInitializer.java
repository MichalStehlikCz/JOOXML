package com.provys.report.jooxml;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import picocli.CommandLine;

import javax.annotation.Nullable;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.File;

@CommandLine.Command(description = "Evaluate JOOXML report", name="jooxml", mixinStandardHelpOptions = true, version = "0.9")
class JooxmlInitializer implements Runnable {

    @CommandLine.Option(names = {"-t", "--template"}, required = true, description = "Template xlsx file")
    private File template;

    @CommandLine.Option(names = {"-d", "--datasource"}, description = "Report datasource definition xml file")
    @Nullable
    private File dataSourceFile;

    @CommandLine.Option(names = {"-b", "--body"}, required = true, description = "Report body xml file")
    private File bodyFile;

    @CommandLine.Option(names = {"-l", "--logfile"}, description = "Log file")
    private File logFile;

    @CommandLine.Option(names = {"--loglevel"}, description = "Log level", defaultValue = "ERROR")
    private Level logLevel;

    @CommandLine.Option(names = {"-p", "--paramfile"}, description = "Parameter XML file")
    private File paramFile;

    @CommandLine.Option(names = {"-c", "--connectstring"}, required = true,
            description = "jdbc connect string to PROVYS Oracle database")
    private String connectString;

    @CommandLine.Option(names = {"-k", "--token"}, required = true,
            description = "token for connection to PROVYS database")
    private String dbToken;

    @CommandLine.Parameters(description = "Output file to be generated")
    private File targetFile;

    /**
     * Configure logger based on command line arguments (logfile and loglevel)
     */
    private void configureLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setConfigurationName("RootLogger");
        AppenderComponentBuilder appenderBuilder;
        if (logFile != null) {
            appenderBuilder = builder.newAppender("Log", "File").
                    addAttribute("fileName", logFile.getPath());
        } else {
            appenderBuilder = builder.newAppender("Log", "Console")
                    .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        }
        appenderBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %-10t %-50c{-2} %msg%n%throwable"));
        builder.add(appenderBuilder);
        builder.add(builder.newRootLogger(logLevel).
                add(builder.newAppenderRef("Log")).
                addAttribute("additivity", true));
        builder.add(builder.newLogger("org.jboss.weld", Level.WARN).
                addAttribute("additivity", true));
        Configurator.initialize(builder.build());
        final Logger logger = LogManager.getLogger(JooxmlInitializer.class);
        logger.info("LoggerInit: Logger initialized: " +
                ((logFile != null) ? "file " + logFile.getPath() : "console") + ", level " + logLevel);
    }

    @Override
    public void run() {
        configureLogger();
        SeContainer container = SeContainerInitializer.newInstance()
                .addProperty("org.jboss.weld.se.archive.isolation", false).initialize();

        RunReport runner = container.select(RunReport.class).get();
        runner.setTemplate(template).
                setBodyFile(bodyFile).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                run();
    }
}
