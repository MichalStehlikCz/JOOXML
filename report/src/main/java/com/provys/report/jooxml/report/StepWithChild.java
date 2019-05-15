package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;

/**
 * Interface represents step with single child and allows navigation to this child
 */
public interface StepWithChild extends ReportStep {

    /**
     * @return child step
     */
    ReportStep getChild();
}
