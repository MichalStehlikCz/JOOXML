package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
interface StepBuilder {

    /**
     * @return parent builder
     */
    @Nonnull
    Optional<StepBuilder> getParent();

    /**
     * @return internal name of step builder
     */
    @Nonnull
    Optional<String> getNameNm();

    /**
     * Set internal name of step builder.
     *
     * @param nameNm is internal name to be assigned to step builder
     * @return self to support fluent build
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
     * Retrieve data source associated with step. Might not be known until validation and if unknown, throws exception.
     *
     * @return data source used for given step. This can be taken from parent step.
     */
    @Nonnull
    ReportDataSource getDataSource();

    /**
     * @return indication if region is top level region (e.g. no region above will initialize rows and region should
     * flush rows)
     */
    boolean isTopLevel();

    /**
     * Validate that builder fulfills conditions for building the step.
     * Validation is called as part of build process as well and usually doesn't have to be called explicitly. Throws
     * exceptions if validation fails. Validation might fill in default values of values taken from parent or children if appropriate.
     *
     * @param dataSources is map of data-sources in report, might be used for data source validation
     */
    void validate(Map<String, ReportDataSource> dataSources);

    /**
     * Evaluate path with structure that will correspond to {@link com.provys.report.jooxml.repexecutor.ExecRegion} map
     * during execution; used in reference translation.
     *
     * @param cellReference is cell reference being translated
     * @return path that can be used to translate this reference during report execution
     */
    @Nonnull
    Optional<AreaCellPath> getPath(CellReference cellReference);

    /**
     * Do actual build of step.
     * Usually called internally from build. Expects that validation passed successfully and might behave unpredictably
     * if builder is in state that is not suitable for validation
     */
    @Nonnull
    ReportStep doBuild(TplWorkbook template);

    /**
     * Validates and builds step from builder.
     */
    @Nonnull
    ReportStep build(Map<String, ReportDataSource> dataSources, TplWorkbook template);
}
