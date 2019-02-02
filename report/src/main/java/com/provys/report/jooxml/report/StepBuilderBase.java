package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue") // we want to allow fluent build even if it is not used now
abstract class StepBuilderBase<T extends StepBuilderBase> implements StepBuilder<T> {

    private static final Logger LOG = LogManager.getLogger(StepBuilderBase.class.getName());
    @Nullable
    private final StepBuilder parent;
    @Nullable
    private String nameNm;

    StepBuilderBase(@Nullable StepBuilder parent) {
        this.parent = parent;
    }

    @Override
    @Nonnull
    public Optional<StepBuilder> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    @Nonnull
    public Optional<String> getNameNm() {
        return Optional.ofNullable(nameNm);
    }

    @Override
    @Nonnull
    public T setNameNm(String nameNm) {
        if (this.nameNm != null) {
            if (this.nameNm.equals(nameNm)) {
                return self();
            }
            LOG.warn("Overwriting already assigned area name - old name {}, new name {}", this.nameNm, nameNm);
        }
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name cannot be empty string");
        }
        this.nameNm = nameNm;
        return self();
    }

    /**
     * @return default name - for root level item, returns default prefix, otherwise asks parent to supply the name
     */
    @Nonnull
    private String getDefaultNameNm() {
        if (parent == null) {
            return getDefaultNameNmPrefix();
        }
        return parent.proposeChildName(this);
    }

    /**
     * @return datasource used for step builder; if step builder doesn't define data source, it inherits one from parent
     * step builder
     */
    @Override
    @Nonnull
    public Optional<ReportDataSource> getDataSource() {
        return getParent().flatMap(StepBuilder::getDataSource);
    }

    /**
     * Method allows fluent build for children of this type
     *
     * @return properly typed self
     */
    @Nonnull
    protected abstract T self();

    /**
     * Validate that builder fulfills conditions for building the step.
     * Base implementation fills in default internal name if one is not specified and calls data set validation.
     * Subclasses might add additional validation rules
     *
     * @param dataSources is map of data-sources in report
     */
    @Override
    public void validate(Map<String, ReportDataSource> dataSources) {
        if (getNameNm().isEmpty()) {
            setNameNm(getDefaultNameNm());
        }
    }

    /**
     * Builds region from this builder. Called from build after validation. Should NOT modify builder - all needed
     * modifications (using default values etc.) should be done as part of validation
     */
    @Nonnull
    protected abstract ReportStep doBuild(TplWorkbook template);

    @Override
    @Nonnull
    public ReportStep build(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        validate(dataSources);
        return doBuild(template);
    }
}
