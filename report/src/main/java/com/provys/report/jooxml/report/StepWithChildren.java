package com.provys.report.jooxml.report;


import com.provys.report.jooxml.repexecutor.ReportStep;

import java.util.stream.Stream;

public interface StepWithChildren extends ReportStep {
    /**
     * @return stream containing children of given step
     */
    Stream<ReportStep> getChildren();
}
