package com.provys.report.jooxml.report;

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

    StepProcessorAncestor(T step, StepContext stepContext) {
        this.step = Objects.requireNonNull(step);
        this.stepContext = Objects.requireNonNull(stepContext);
    }

    /**
     * @return step this processor is associated with
     */
    @Nonnull
    T getStep() {
        return step;
    }

    /**
     * @return step context for this step
     */
    @Nonnull
    StepContext getStepContext() {
        return stepContext;
    }

    @Override
    public void execute() {
        throw new RuntimeException(this.getClass().getSimpleName() +
                " processor should be expanded during pipeline processing");
    }
}
