package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public interface StepBuilder {

    /**
     * @return parent builder
     */
    @Nonnull
    Optional<StepBuilder> getParent();

    /**
     * Return internal name of step builder.
     *
     * @return internal name
     */
    @Nonnull
    Optional<String> getNameNm();

    /**
     * Set internal name of step builder.
     *
     * @param nameNm is internal name to be assigned to step builder
     * @throws IllegalArgumentException if nameNm parameter contains empty String
     */
    @Nonnull
    StepBuilder setNameNm(String nameNm);

    /**
     * @return default prefix for step of this type
     */
    @Nonnull
    String getDefaultNameNmPrefix();

    /**
     * Return valid name for child step. Child can supply prefix, in that case it will build child name based on this
     * prefix. Step has to ensure that child names will not collide.
     */
    @Nonnull
    String proposeChildName(StepBuilder child);

    /**
     * @return datasource used for step builder; if step builder doesn't define data source, it inherits one from parent
     * step builder
     */
    @Nonnull
    Optional<ReportDataSource> getDataSource();

    /**
     * @return indication if region is top level region (e.g. no region above will initialize rows and region should
     * flush rows)
     */
    boolean isTopLevel();

    /**
     * Validate that builder fulfills conditions for building the step.
     * Validation is called as part of build process as well. Throws exceptions if validation fails
     *
     * @param dataSources is map of data-sources in report, used to validate data source name and parent
     */
    void validate(Map<String, ReportDataSource> dataSources);

    /**
     * Builds report step
     */
    @Nonnull
    ReportStep build(Map<String, ReportDataSource> dataSources, TplWorkbook template);
}