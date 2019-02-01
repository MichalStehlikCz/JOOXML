package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

class DataReader extends Step {

    @Nonnull
    private final ReportDataSource dataSource;
    @Nonnull
    private final Step child;

    DataReader(String nameNm, ReportDataSource dataSource, Step child) {
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
        return pipeline.map(stepProcessor -> ((stepProcessor instanceof DataReaderProcessor)
                && (((DataReaderProcessor) stepProcessor).getStep() == this))
                ? ((DataReaderProcessor) stepProcessor).apply() : stepProcessor);
    }

    @Override
    public StepProcessor getProcessor(StepContext stepContext) {
        return new DataReaderProcessor(this, stepContext);
    }
}
