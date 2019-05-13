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
abstract class StepBuilderBase<T extends StepBuilderBase> implements StepBuilder {

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
    public StepBuilder getRoot() {
        return getParent().map(StepBuilder::getRoot).orElse(this);
    }

    @Override
    public boolean isAncestor(StepBuilder step) {
        return getParent().map(p -> (p == step) || p.isAncestor(step)).orElse(false);
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
    public ReportDataSource getDataSource() {
        if (parent == null) {
            throw new RuntimeException("Cannot retrieve datasource - parent not set");
        }
        return parent.getDataSource();
    }

    /**
     * Method allows fluent build for children of this type
     *
     * @return properly typed self
     */
    @Nonnull
    protected abstract T self();

    /**
     * Validates that parent is specified.
     * Must be overriden in root step builder as root step doesn't have parent
     */
    protected void validateParent() {
        if (getParent().isEmpty()) {
            throw new RuntimeException("Step other than root should have parent");
        }
    }

    /**
     * Procedure does actual validation of object itself.
     * Base implementation fills in default internal name if one is not specified.
     * Subclasses might add additional validation rules
     *
     * @param dataSources is map of data-sources in report
     */
    protected void doValidate(Map<String, ReportDataSource> dataSources) {
        if (getNameNm().isEmpty()) {
            setNameNm(getDefaultNameNm());
        }
        validateParent();
    }

    /**
     * Procedure is called once validtion of object itself was successful.
     * Can validate children or other outside objects connected to step builder
     */
    protected void afterValidate(Map<String, ReportDataSource> dataSources, TplWorkbook template) {}

    /**
     * Validate that builder fulfills conditions for building the step.
     * Actual validation should be part of doValidate; if completed successfully, afterValidate is invoked - this can be
     * used e.g. to validate children once object itself has been found valid.
     *
     * @param dataSources is map of data-sources in report
     */
    @Override
    public void validate(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        doValidate(dataSources);
        afterValidate(dataSources, template);
    }

    @Nonnull
    @Override
    public ReportStep build(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        validate(dataSources, template);
        return doBuild(template);
    }

    @Override
    public String toString() {
        return "StepBuilderBase{" +
                "nameNm='" + nameNm + '\'' +
                '}';
    }
}
