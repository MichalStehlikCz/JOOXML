package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Parent step is equivalent to sequence of its children; the children will be expanded and processed in the same
 * context (with the exception of execution region, where child is appended to parent region). Parent step does not care
 * if processed regions are horizontal, vertical, various pages... anything
 */
class ParentStep extends Step {

    private final List<ReportStep> children;

    ParentStep(String nameNm, Collection<ReportStep> children) {
        super(nameNm);
        this.children = new ArrayList<>(children);
    }

    @Override
    public int getNeededProcessorApplications() {
        // need to expand this step's processor + all children
        return getChildren().map(ReportStep::getNeededProcessorApplications).max(Integer::compareTo).orElse(0)
                + 1;
    }

    /**
     * Build step processor that will get expanded to children during step processing.
     *
     * @param stepContext is context step processor will get assigned
     * @return step processor that will be expanded to children during step processing
     */
    @Override
    public StepProcessor getProcessor(StepContext stepContext) {
        return new ParentProcessor(this, stepContext);
    }

    /**
     * @return stream containing children of this step
     */
    private Stream<ReportStep> getChildren() {
        return Collections.unmodifiableList(children).stream();
    }

    private static class ParentProcessor extends StepProcessorAncestor<ParentStep> {

        ParentProcessor(ParentStep step, StepContext stepContext) {
            super(step, stepContext);
        }

        /**
         * Expands step processor to child processors if its step is passed as parameter
         *
         * @return stream of step processors for children of given step
         */
        public Stream<StepProcessor> apply() {
            return getStep().getChildren().map(childStep -> childStep.getProcessor(getStepContext()));
        }
    }
}
