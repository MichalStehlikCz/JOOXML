package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportStep;

public interface StepBuilder {
    /**
     * Builds report step
     */
    public ReportStep build(TemplateWorkbook template);
}
