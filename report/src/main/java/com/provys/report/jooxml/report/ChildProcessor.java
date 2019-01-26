package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import java.util.Objects;
import java.util.stream.Stream;

public class ChildProcessor extends StepProcessorAncestor {

    private final StepWithChildren step;

    ChildProcessor(StepWithChildren step, StepContext stepContext) {
        super(stepContext);
        this.step = Objects.requireNonNull(step);
    }

    /**
     * @return step this processor is associated with
     */
    StepWithChildren getStep() {
        return step;
    }

    /**
     * Expands step processor to child processors if its step is passed as parameter
     *
     * @return stream of step processors for children of given step
     */
    Stream<StepProcessor> expand() {
        return getStep().getChildren().map((childStep) -> childStep.getProcessorSupplier().apply(getStepContext()));
    }

    @Override
    public void execute() {
        throw new RuntimeException("Child processor should be expanded during pipeline processing");
    }
}
