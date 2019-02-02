package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class RowRepeaterBuilder extends RowRegionBuilder<RowRepeaterBuilder> {


    RowRepeaterBuilder(@Nullable StepBuilder parent) {
        super(parent);
    }

    @Nonnull
    @Override
    public String getDefaultNameNmPrefix() {
        return "REPEATER";
    }

    @Nonnull
    @Override
    public String proposeChildName(StepBuilder child) {
        return child.getDefaultNameNmPrefix();
    }

    @Nonnull
    @Override
    protected RowRepeaterBuilder self() {
        return this;
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        return null;
    }

}
