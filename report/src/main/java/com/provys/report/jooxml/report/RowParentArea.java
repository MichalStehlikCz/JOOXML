package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

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
    public StepProcessor getProcessor(StepContext stepContext) {
        return new ChildProcessor(this, stepContext);
    }

    @Override
    public Stream<ReportStep> getChildren() {
        return Collections.unmodifiableList(children).stream();
    }
}
