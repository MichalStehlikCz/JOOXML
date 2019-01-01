package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;

public interface StepBuilder {
    /**
     * Builds report step
     */
    public ReportStep build(TemplateWorkbook template);
}
