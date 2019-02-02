package com.provys.report.jooxml.report;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Adds methods for accessing and setting effective first and last row of step usable during validation.
 */
interface RowStepBuilder extends StepBuilder {

    /**
     * @return first row covered by area - effective value, potentially calculated from children; returns empty Optional
     * if it is unknown
     */
    @Nonnull
    abstract Optional<? extends Integer> getEffFirstRow();

    /**
     * Set the effective first row in template covered by this step.
     * It is only allowed to call this function during validation. Earlier calls might fail with IllegalStateException
     *
     * @param effFirstRow is the first row to be covered by given step
     * @throws IllegalStateException if builder is unable to process this command at this stage. It MUST be able to
     * process this command during validation but value might be kept in children and if children are not initialized
     * yet, call to this function might fail
     */
    abstract void setEffFirstRow(Integer effFirstRow);

    /**
     * @return last row in template covered by area - effective value, potentially calculated from children; returns
     * empty Optional if it is unknown
     */
    @Nonnull
    abstract Optional<? extends Integer> getEffLastRow();

    /**
     * Set the effective last row in template covered by step.
     *
     * @param effLastRow is last row in template covered by region
     */
    abstract void setEffLastRow(Integer effLastRow);
}
