package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import java.util.Objects;
import java.util.stream.Stream;

class ChildProcessor extends StepProcessorAncestor<StepWithChildren> {

    ChildProcessor(StepWithChildren step, StepContext stepContext) {
        super(step, stepContext);
    }

    /**
     * Expands step processor to child processors if its step is passed as parameter
     *
     * @return stream of step processors for children of given step
     */
    Stream<StepProcessor> expand() {
        return getStep().getChildren().map((childStep) -> childStep.getProcessor(getStepContext()));
    }
}
