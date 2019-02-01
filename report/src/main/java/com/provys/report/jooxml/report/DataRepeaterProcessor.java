package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

class DataRepeaterProcessor extends StepProcessorAncestor<DataRepeater> {

        DataRepeaterProcessor(DataRepeater step, StepContext stepContext) {
            super(step, stepContext);
        }

        /**
         * Expands step processor to child processors if its step is passed as parameter
         *
         * @return stream of step processors for children of given step
         */
        @Nonnull
        Stream<StepProcessor> expand() {
            return getStep().getDataSource().getDataContext().
                    execute(getStepContext().getData()).
                    map(dataRecord -> getStep().getChild().getProcessor(
                            new StepContext(getStepContext().getReportContext(), dataRecord,
                                    getStepContext().getCoordinates())));
        }

        @Override
        public void execute() {
            throw new RuntimeException("Data reader processor should be expanded during pipeline processing");
        }
}
