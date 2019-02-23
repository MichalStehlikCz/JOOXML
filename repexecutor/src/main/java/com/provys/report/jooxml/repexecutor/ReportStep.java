package com.provys.report.jooxml.repexecutor;

import java.util.stream.Stream;

/**
 * Describes step in report execution.
 * Steps are immutable classes that form report definition and support report processing. Implementations of this
 * interface might retrieve data from dataset, copy cells from template to target worksheet and populate with data etc.
 * Report references its root step, steps generally form tree, even though sub-steps are kept private and not made
 * available through this interface.
 *
 * Step processing works as follows
 * - step can produce processor, using method {@code getProcessor}, that will represent processing of given step in
 * report processing pipeline; externally, stream is initiated by supplication of processor of the root step
 * - method {@code addStepProcessing} adds processing for given step to processing pipeline (stream). In case of
 * terminal step (step that modifies target Excel sheet on execution of its processor), method should do nothing. For
 * intermediary step, step processing should replace processor of given step with its expansion (e.g. processors of
 * child steps, ...). Note that method should only use processors of child steps for replacement, it should never
 * use processor of parent step as system expects it has eben processed already
 */
public interface ReportStep {

    /**
     * @return internal name of given region, used as region identification, must be unique between children of given
     * parent
     */
    String getNameNm();

    /**
     * @return number of step processor apply invocations needed to fully expand this step and all its sub-steps.
     */
    int getNeededProcessorApplications();

    /**
     * Get step processor for given step and specified context
     *
     * @param stepContext is context step processor will get assigned
     * @param parentRegion is execution region address of parent processor; new processor should create its own region
     *                  reference and add it to this execution region
     * @return step processor that will represent this step in processing pipeline
     */
    StepProcessor getProcessor(StepContext stepContext, ExecRegion parentRegion);
}
