package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.workbook.CellAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
@SuppressWarnings("UnusedReturnValue")
abstract class RowAreaBuilder<T extends RowAreaBuilder> extends StepBuilderBase<T> {

    private static final Logger LOG = LogManager.getLogger(RowAreaBuilder.class.getName());

    private boolean topLevel = false;
    @Nullable
    private Integer firstRow = null;
    @Nullable
    private Integer lastRow = null;

    RowAreaBuilder(@Nullable StepBuilder parent) {
        super(parent);
        if (parent == null) {
            // root region has some defaults...
            topLevel = true;
            firstRow = 0;
            lastRow = Integer.MAX_VALUE;
        }
    }

    /**
     * @return first row in template covered by area; returns -1 if it has not been initialized yet
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
    T setLastRow(int lastRow) {
        if (lastRow < 0) {
            throw new IllegalArgumentException("Last row cannot be negative");
        }
        this.lastRow = lastRow;
        return self();
    }

    /**
     * @return indication if region is top level region (e.g. no region above will initialize rows and region should
     * flush rows)
     */
    boolean isTopLevel() {
        return topLevel;
    }

    /**
     * Sets indication that region is top level region
     *
     * @param topLevel flag indicating if reion should be considered top level or not
     */
    T setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
        return self();
    }

    /**
     * Validates all properties of this region.
     * Base implementation validates all field binds and subregions. Subclasses might add additional rules on what is
     * actually considered valid region definition
     */
    @Override
    protected void validate() {
        super.validate();
        if (getFirstRow().orElseThrow(
                () -> new IllegalStateException("First row of covered area has not been initialized"))
                > getLastRow().orElseThrow(
                () -> new IllegalStateException("Last row of covered area has not been initialized"))) {
            throw new IllegalStateException("First row of region has to be above last row");
        }
    }
}
