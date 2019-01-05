package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class StepBuilderBase<T extends StepBuilderBase> implements StepBuilder {

    private final Logger LOG = LogManager.getLogger(StepBuilderBase.class.getName());
    @Nullable
    protected final StepBuilder parent;
    @Nullable
    private String nameNm;

    public StepBuilderBase(@Nullable StepBuilder parent) {
        this.parent = parent;
    }

    @Override
    public Optional<StepBuilder> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * @return default name - for root level item, returns default prefix, otherwise asks parent to supply the name
     */
    protected String getDefaultNameNm() {
        if (parent == null) {
            return getDefaultNameNmPrefix();
        }
        return parent.proposeChildName(this);
    }

    /**
     * @return internal name of row area
     */
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
    abstract protected ReportStep doBuild(TplWorkbook template);

    /**
     * Method allows fluent build for children of this type
     *
     * @return properly typed self
     */
    abstract protected T self();

    /**
     * Validates and builds step from builder.
     */
    public ReportStep build(TplWorkbook template) {
        validate();
        return doBuild(template);
    }
}
