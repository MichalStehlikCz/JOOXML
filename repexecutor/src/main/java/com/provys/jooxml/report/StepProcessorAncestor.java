package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.StepContext;
import com.provys.jooxml.repexecutor.StepProcessor;

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
