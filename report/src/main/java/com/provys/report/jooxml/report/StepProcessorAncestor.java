package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class StepProcessorAncestor<T extends ReportStep> implements StepProcessor {

    @Nonnull
    private final StepContext stepContext;
    @Nonnull
    private final T step;
    @Nonnull
    private final ExecRegionContext execRegionContext;

    StepProcessorAncestor(T step, StepContext stepContext, ExecRegionContext execRegionContext) {
        this.step = Objects.requireNonNull(step);
        this.stepContext = Objects.requireNonNull(stepContext);
        this.execRegionContext = Objects.requireNonNull(execRegionContext);
    }

    /**
     * @return step this processor is associated with
     */
    @Nonnull
    @Override
    public T getStep() {
        return step;
    }

    /**
     * @return step context for this step
     */
    @Nonnull
    StepContext getStepContext() {
        return stepContext;
    }

    /**
     * @return execution region context for this step processor
     */
    @Nonnull
    ExecRegionContext getExecRegionContext() {
        return execRegionContext;
    }

    /**
     * @return execution region for this step processor (taken from execution region context)
     */
    @Nonnull
    ExecRegion getExecRegion() {
        return execRegionContext.getExecRegion();
    }

    @Override
    public void execute() {
        throw new RuntimeException(this.getClass().getSimpleName() +
                " processor should be expanded during pipeline processing");
    }
}
