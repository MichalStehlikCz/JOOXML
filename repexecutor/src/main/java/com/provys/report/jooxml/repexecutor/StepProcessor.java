package com.provys.report.jooxml.repexecutor;

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
     * Execution of step should push data to target worksheet
     */
    void execute();
}
