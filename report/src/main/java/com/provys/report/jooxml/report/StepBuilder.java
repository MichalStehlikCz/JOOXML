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
    public Optional<ReportDataSource> getDataSource();

    /**
     * Builds report step
     */
    @Nonnull
    ReportStep build(Map<String, ReportDataSource> dataSources, TplWorkbook template);
}