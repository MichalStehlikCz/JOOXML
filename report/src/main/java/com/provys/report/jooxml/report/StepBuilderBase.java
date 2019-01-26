package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

abstract class StepBuilderBase<T extends StepBuilderBase> implements StepBuilder {

    private static final Logger LOG = LogManager.getLogger(StepBuilderBase.class.getName());
    @Nullable
    protected final StepBuilder parent;
    @Nullable
    private String nameNm;

    StepBuilderBase(@Nullable StepBuilder parent) {
        this.parent = parent;
    }

    @Override
    public Optional<StepBuilder> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * @return default name - for root level item, returns default prefix, otherwise asks parent to supply the name
     */
    @Nonnull
    protected String getDefaultNameNm() {
        if (parent == null) {
            return getDefaultNameNmPrefix();
        }
        return parent.proposeChildName(this);
    }

    /**
     * @return internal name of row area
     */
    @Nonnull
    @Override
    public Optional<String> getNameNm() {
        return Optional.ofNullable(nameNm);
    }

    /**
     * Set internal name of report region
     *
     * @param nameNm is new internal name
     * @return self to allow fluent build
     */
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
     * Validates all properites of this region.
     * Base implementation fills in default internal name if one is not specified. Subclasses might add additional
     * validation rules
     */
    protected void validate() {
        if (getNameNm().isEmpty()) {
            setNameNm(getDefaultNameNm());
        }
    }

    /**
     * Builds region from this builder. Called from build after validation.
     */
    @Nonnull
    protected abstract ReportStep doBuild(TplWorkbook template);

    /**
     * Method allows fluent build for children of this type
     *
     * @return properly typed self
     */
    @Nonnull
    protected abstract T self();

    /**
     * Validates and builds step from builder.
     */
    @Nonnull
    public ReportStep build(TplWorkbook template) {
        validate();
        return doBuild(template);
    }
}
