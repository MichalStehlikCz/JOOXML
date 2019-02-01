package com.provys.report.jooxml.report;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Class is used to build ReportStep based on underlying template workbook and information about binds and subregions.
 * It provides all necessary validations and at the end, creates ReportStep of specific type, that is non-mutable
 * and supplies view of underlying data useful during report execution.
 *
 * All cell coordinates correspond to position in template workbook; they are recalculated to positions relative to
 * parent region when building ReportStep.
 *
 * Class is not thread-safe, it is expected to onl be used when building report definition within single thread
 */
@SuppressWarnings("UnusedReturnValue") // we want to enable fluent build
abstract class RowStepBuilderBase<T extends RowStepBuilderBase> extends StepBuilderBase<T> implements RowStepBuilder {

    @Nullable
    private Integer firstRow = null;
    @Nullable
    private Integer lastRow = null;

    RowStepBuilderBase(@Nullable StepBuilder parent) {
        super(parent);
        if (parent == null) {
            // root region has some defaults...
            firstRow = 0;
            lastRow = Integer.MAX_VALUE;
        }
    }

    /**
     * @return first row in template covered by area; returns empty Optional if it has not been initialized yet
     */
    @Nonnull
    Optional<Integer> getFirstRow() {
        return Optional.ofNullable(firstRow);
    }

    /**
     * Set first row in template covered by region.
     *
     * @param firstRow is first row covered by given region
     * @return self to allow fluent build
     */
    @Nonnull
    T setFirstRow(Integer firstRow) {
        if (firstRow < 0) {
            throw new IllegalArgumentException("First row cannot be negative");
        }
        this.firstRow = firstRow;
        return self();
    }

    /**
     * @return the first row value that has been set in this builder
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffFirstRow() {
        return getFirstRow();
    }

    /**
     * Setting effective first row is implemented by setting the first row directly in this builder
     *
     * @param effFirstRow is value to be set to the first row
     */
    @Override
    public void setEffFirstRow(Integer effFirstRow) {
        setFirstRow(effFirstRow);
    }

    /**
     * @return last row in template covered by area; returns -1 if it has not been initialized et
     */
    @Nonnull
    Optional<Integer> getLastRow() {
        return Optional.ofNullable(lastRow);
    }

    /**
     * Set last row in template covered by region.
     *
     * @param lastRow is last row in template coveerd by region
     * @return self to allow fluent build
     */
    @Nonnull
    T setLastRow(Integer lastRow) {
        if (lastRow < 0) {
            throw new IllegalArgumentException("Last row cannot be negative");
        }
        this.lastRow = lastRow;
        return self();
    }

    /**
     * @return the last row value that has been set in this builder
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffLastRow() {
        return getLastRow();
    }

    /**
     * Setting effective last row is implemented by setting the last row directly in this builder
     *
     * @param effLastRow is value to be set to the last row
     */
    @Override
    public void setEffLastRow(Integer effLastRow) {
        setFirstRow(effLastRow);
    }

    /**
     * @return top level flag value from parent region or true for root level region as if row region parent is top
     * level, child regions will also produce top level rows
     */
    @Override
    public boolean isTopLevel() {
        return getParent().map(StepBuilder::isTopLevel).orElse(true);
    }

    /**
     * Validates all properties of this region.
     * Base implementation validates effective row range. Subclasses might add additional rules on what is
     * actually considered valid region definition (e.g. validation of sub-regions, binds, ...)
     */
    @Override
    protected void validate() {
        super.validate();
        if (getEffFirstRow().orElseThrow(
                () -> new IllegalStateException("First row of covered area has not been initialized"))
                > getEffLastRow().orElseThrow(
                () -> new IllegalStateException("Last row of covered area has not been initialized"))) {
            throw new IllegalStateException("First row of region has to be above last row");
        }
    }
}
