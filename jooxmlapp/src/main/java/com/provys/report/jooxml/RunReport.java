package com.provys.report.jooxml;

import com.provys.provysdb.ProvysDbContext;
import com.provys.report.jooxml.datasource.DataSourceFactory;
import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.report.ReportFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
class RunReport implements Runnable {

    @Nonnull
    private final RepExecutor executor;
    @Nonnull
    private final ReportFactory reportFactory;
    @Nonnull
    private final DataSourceFactory dataSourceFactory;
    @Nullable
    private File template;
    @Nullable
    private File dataSourceFile;
    @Nullable
    private File bodyFile;
    @Nullable
    private File paramFile;
    @Nullable
    private File targetFile;
    @Nullable
    private String provysAddress;
    @Nullable
    private String provysUser;
    @Nullable
    private String provysPwd;
    @Nullable
    private String dbToken;

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    RunReport(RepExecutor executor, ReportFactory reportFactory, DataSourceFactory dataSourceFactory,
              ProvysDbContext dbContext) {
        this.executor = Objects.requireNonNull(executor);
        this.reportFactory = Objects.requireNonNull(reportFactory);
        this.dataSourceFactory = Objects.requireNonNull(dataSourceFactory);
    }

    RunReport setTemplate(File template) {
        this.template = template;
        return this;
    }

    RunReport setDataSourceFile(@Nullable File dataSourceFile) {
        this.dataSourceFile = dataSourceFile;
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

    RunReport setProvysAddress(String provysAddress) {
        this.provysAddress = provysAddress;
        return this;
    }

    RunReport setProvysUser(String provysUser) {
        this.provysUser = provysUser;
        return this;
    }

    RunReport setProvysPwd(String provysPwd) {
        this.provysPwd = provysPwd;
        return this;
    }

    /**
     * Set value of field dbToken
     *
     * @param dbToken is new value to be set
     * @return self to enable chaining
     */
    RunReport setDbToken(@Nullable String dbToken) {
        this.dbToken = dbToken;
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
    @ActivateRequestContext
    public void run() {
        addLoggerShutdownHook();
        ConfigProviderResolver.instance().registerConfig(
                ConfigProviderResolver.instance().getBuilder().forClassLoader(getClass().getClassLoader()).
                        withSources(new CommandLineParamsSource(provysAddress, provysUser, provysPwd)).build(),
                getClass().getClassLoader());
        Report report;
        if (dataSourceFile != null) {
            report = reportFactory.build(dataSourceFile, bodyFile, template);
        } else {
            report = reportFactory.build(dataSourceFactory.getRootDataSource(), bodyFile, template);
        }
        executor.setReport(report).
                setParamFile(paramFile).
                setTargetFile(targetFile).
                setDbToken(dbToken).
                run();
    }

    public static class CommandLineParamsSource implements ConfigSource {

        private final Map<String, String> properties = new HashMap<>(3);

        CommandLineParamsSource(String url, String user, String pwd) {
            properties.put("PROVYSDB_URL", url);
            properties.put("PROVYSDB_USER", user);
            properties.put("PROVYSDB_PWD", pwd);
        }

        @Override
        public int getOrdinal() {
            return 900;
        }

        @Override
        public Map<String, String> getProperties() {
            return properties;
        }

        @Override
        public String getValue(String key) {
            return properties.get(key);
        }

        @Override
        public String getName() {
            return "CommandLineParams";
        }
    }
}
