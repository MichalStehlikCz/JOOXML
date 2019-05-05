package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Step built on data source, repeats child for each row of data
 */
class DataRepeater extends DataStep {

    DataRepeater(String nameNm, ReportDataSource dataSource, ReportStep child) {
        super(nameNm, dataSource, child);
    }

    @Override
    public StepProcessor getProcessor(StepContext stepContext, ExecRegion parentRegion) {
        return new DataRepeaterProcessor(this, stepContext, parentRegion);
    }

    private static class DataRepeaterProcessor extends StepProcessorAncestor<DataRepeater> {

        DataRepeaterProcessor(DataRepeater step, StepContext stepContext, ExecRegion parentRegion) {
            super(step, stepContext, parentRegion.addTable(step.getNameNm()));
        }

        /**
         * Expands step processor to child processors if its step is passed as parameter
         *
         * @return stream of step processors for children of given step
         */
        @Nonnull
        @Override
        public Stream<StepProcessor> apply() {
            DataCursor dataCursor = getStepContext().getReportContext().getDataContext(getStep().getDataSource()).
                    execute(getStepContext().getData());
            return Stream.concat(dataCursor.getData().map(dataRecord -> getStep().getChild().getProcessor(
                            getStepContext().cloneWithReplaceData(dataRecord), getExecRegion())),
                    Stream.of(new CloseDataCursorProcessor(getStep(), dataCursor)));
        }

        @Override
        public void execute() {
            throw new RuntimeException("Data reader processor should be expanded during pipeline processing");
        }
    }
}
