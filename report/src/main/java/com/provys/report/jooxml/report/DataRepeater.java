package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Step built on data source, repeats child for each row of data
 */
class DataRepeater extends Step {

    @Nonnull
    private final ReportDataSource dataSource;
    @Nonnull
    private final ReportStep child;

    DataRepeater(String nameNm, ReportDataSource dataSource, ReportStep child) {
        super(nameNm);
        this.dataSource = Objects.requireNonNull(dataSource);
        this.child = Objects.requireNonNull(child);
    }

    @Nonnull
    ReportDataSource getDataSource() {
        return dataSource;
    }

    @Nonnull
    ReportStep getChild() {
        return child;
    }

    @Override
    public Stream<StepProcessor> addStepProcessing(Stream<StepProcessor> pipeline) {
        return pipeline.flatMap(stepProcessor -> ((stepProcessor instanceof DataRepeaterProcessor)
                && (((DataRepeaterProcessor) stepProcessor).getStep() == this))
                ? ((DataRepeaterProcessor) stepProcessor).expand() : Stream.of(stepProcessor));
    }

    @Override
    public StepProcessor getProcessor(StepContext stepContext) {
        return new DataRepeaterProcessor(this, stepContext);
    }

    private static class DataRepeaterProcessor extends StepProcessorAncestor<DataRepeater> {

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
}
