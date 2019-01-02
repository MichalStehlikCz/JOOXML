package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

public interface StepBuilder {
    /**
     * Builds report step
     */
    public ReportStep build(TplWorkbook template);
}
