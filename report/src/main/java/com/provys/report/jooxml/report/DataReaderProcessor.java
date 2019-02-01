package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;

public class DataReaderProcessor extends StepProcessorAncestor<DataReader> {

    DataReaderProcessor(DataReader step, StepContext stepContext) {
        super(step, stepContext);
    }

    /**
     * Expands step processor to child processors if its step is passed as parameter
     *
     * @return stream of step processors for children of given step
     */
    @Nonnull
    StepProcessor apply() {
        DataRecord dataRecord = getStep().getDataSource().getDataContext().
                execute(getStepContext().getData()).
                reduce((u, v) -> {
                    throw new RuntimeException("Dataset should only return single row, but returned more");
                }).orElseThrow(() -> new RuntimeException("Dataset did not return any data"));
        StepContext context = new StepContext(getStepContext().getReportContext(), dataRecord,
                getStepContext().getCoordinates());
        return getStep().getChild().getProcessor(getStepContext());
    }
}
