package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

class DataReader extends DataStep {

    DataReader(String nameNm, ReportDataSource dataSource, ReportStep child) {
        super(nameNm, dataSource, child);
    }

    @Override
    public StepProcessor getProcessor(StepContext stepContext, ExecRegionContext parentRegionContext) {
        return new DataReaderProcessor(this, stepContext, parentRegionContext);
    }

    private static class DataReaderProcessor extends StepProcessorAncestor<DataReader> {

        private static final Logger LOG = LogManager.getLogger(DataReaderProcessor.class);

        DataReaderProcessor(DataReader step, StepContext stepContext, ExecRegionContext parentRegionContext) {
            super(step, stepContext, parentRegionContext.addRegion(step.getNameNm(), 1));
        }

        /**
         * Expands step processor to child processors if its step is passed as parameter
         *
         * @return stream of step processors for children of given step
         */
        @Nonnull
        public Stream<StepProcessor> apply() {
            LOG.trace("Apply DataReaderProcesor {}", this::getStep);
            DataRecord dataRecord;
            try (var dataCursor = getStepContext().getReportContext().getDataContext(getStep().getDataSource()).
                    execute(getStepContext().getData())) {
                dataRecord = dataCursor.getData().
                        reduce((u, v) -> {
                            throw new RuntimeException("Dataset should only return single row, but returned more");
                        }).orElseThrow(() -> new RuntimeException("Dataset did not return any data"));
            }
            var context = getStepContext().cloneWithReplaceData(dataRecord);
            return Stream.of(getStep().getChild().getProcessor(context, getExecRegionContext()));
        }
    }}
