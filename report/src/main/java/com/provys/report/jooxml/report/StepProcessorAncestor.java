package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ExecRegion;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class StepProcessorAncestor<T extends ReportStep> implements StepProcessor {

    @Nonnull
    private final StepContext stepContext;
    @Nonnull
    private final T step;
    @Nonnull
    private final ExecRegion execRegion;

    StepProcessorAncestor(T step, StepContext stepContext, ExecRegion execRegion) {
        this.step = Objects.requireNonNull(step);
        this.stepContext = Objects.requireNonNull(stepContext);
        this.execRegion = Objects.requireNonNull(execRegion);
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
     * @return execution region for this step processor
     */
    @Nonnull
    ExecRegion getExecRegion() {
        return execRegion;
    }

    @Override
    public void execute() {
        throw new RuntimeException(this.getClass().getSimpleName() +
                " processor should be expanded during pipeline processing");
    }
}
