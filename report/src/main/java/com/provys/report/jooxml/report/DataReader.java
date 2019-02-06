package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

class DataReader extends Step {

    @Nonnull
    private final ReportDataSource dataSource;
    @Nonnull
    private final ReportStep child;

    DataReader(String nameNm, ReportDataSource dataSource, ReportStep child) {
        super(nameNm);
        this.dataSource = Objects.requireNonNull(dataSource);
        this.child = Objects.requireNonNull(child);
    }

    @Nonnull
    private ReportDataSource getDataSource() {
        return dataSource;
    }

    @Nonnull
    private ReportStep getChild() {
        return child;
    }

    @Override
    public int getNeededProcessorApplications() {
        return getChild().getNeededProcessorApplications() + 1;
    }

    @Override
    public StepProcessor getProcessor(StepContext stepContext) {
        return new DataReaderProcessor(this, stepContext);
    }

    private static class DataReaderProcessor extends StepProcessorAncestor<DataReader> {

        DataReaderProcessor(DataReader step, StepContext stepContext) {
            super(step, stepContext);
        }

        /**
         * Expands step processor to child processors if its step is passed as parameter
         *
         * @return stream of step processors for children of given step
         */
        @Nonnull
        public Stream<StepProcessor> apply() {
            DataRecord dataRecord = getStepContext().getReportContext().getDataContext(getStep().getDataSource()).
                    execute(getStepContext().getData()).
                    reduce((u, v) -> {
                        throw new RuntimeException("Dataset should only return single row, but returned more");
                    }).orElseThrow(() -> new RuntimeException("Dataset did not return any data"));
            StepContext context = getStepContext().cloneWithReplaceData(dataRecord);
            return Stream.of(getStep().getChild().getProcessor(context));
        }
    }}
