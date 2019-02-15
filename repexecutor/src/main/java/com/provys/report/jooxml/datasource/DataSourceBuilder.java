package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
interface DataSourceBuilder {

    /**
     * Get parent builder
     */
    @Nonnull
    Optional<DataSourceBuilder> getParent();

    /**
     * @return internal name of this builder, empty optional if not initialized yet
     */
    @Nonnull
    Optional<String> getNameNm();

    /**
     * Validate that builder fulfills conditions for building the step.
     * Validation is called as part of build process as well and usually doesn't have to be called explicitly. Throws
     * exceptions if validation fails. Validation might fill in default values of values taken from parent or children
     * if appropriate.
     */
    void validate();

    /**
     * Do actual build of {@code ReportDataSource}.
     * Usually called internally from build. Expects that validation passed successfully and might behave unpredictably
     * if builder is in state that is not suitable for validation
     */
    @Nonnull
    ReportDataSource doBuild(@Nullable ReportDataSource parent);

    /**
     * Validates and builds {@code ReportDataSource} from builder.
     */
    @Nonnull
    ReportDataSource build(@Nullable ReportDataSource parent);
}
