package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
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
    private final Step child;

    DataRepeater(String nameNm, ReportDataSource dataSource, Step child) {
        super(nameNm);
        this.dataSource = Objects.requireNonNull(dataSource);
        this.child = Objects.requireNonNull(child);
    }

    @Nonnull
    ReportDataSource getDataSource() {
        return dataSource;
    }

    @Nonnull
    Step getChild() {
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
}
