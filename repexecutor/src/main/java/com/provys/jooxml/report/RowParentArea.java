package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportStep;
import com.provys.jooxml.repexecutor.StepContext;
import com.provys.jooxml.repexecutor.StepProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class RowParentArea extends Step implements StepWithChildren {

    private final List<ReportStep> children;

    RowParentArea(String nameNm, Collection<ReportStep> children) {
        super(nameNm);
        this.children = new ArrayList<>(children);
    }

    @Override
    public Stream<StepProcessor> addStepProcessing(Stream<StepProcessor> pipeline) {
        return pipeline.flatMap(stepProcessor -> ((stepProcessor instanceof ChildProcessor)
                && (((ChildProcessor) stepProcessor).getStep() == this))
                ? ((ChildProcessor) stepProcessor).expand() : Stream.of(stepProcessor));
    }

    @Override
    public Function<StepContext, StepProcessor> getProcessorSupplier() {
        return (context) -> new ChildProcessor(this, context);
    }

    @Override
    public Stream<ReportStep> getChildren() {
        return Collections.unmodifiableList(children).stream();
    }
}
