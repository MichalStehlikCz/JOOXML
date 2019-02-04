package com.provys.report.jooxml.repexecutor;

import java.util.stream.Stream;

/**
 * Represents item representing report step in processing pipeline.
 * Step rpocessor is usually implemented as inner class in corresponding report step class
 */
public interface StepProcessor {

    /**
     * @return step this processor represents in processing pipeline
     */
    ReportStep getStep();

    /**
     * Process intermediary step processor. This action generally translates step processor to processors, corresponding
     * to its sub-steps, potentially with modified data
     */
    Stream<StepProcessor> apply();

    /**
     * Execution of step should push data to target worksheet
     */
    void execute();
}
