package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import java.util.Objects;

abstract class StepProcessorAncestor implements StepProcessor {

    private final StepContext stepContext;

    StepProcessorAncestor(StepContext stepContext) {
        this.stepContext = Objects.requireNonNull(stepContext);
    }

    /**
     * @return step context for this step
     */
    protected StepContext getStepContext() {
        return stepContext;
    }
}
