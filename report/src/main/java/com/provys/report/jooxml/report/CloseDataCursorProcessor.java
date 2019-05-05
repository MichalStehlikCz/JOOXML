package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class is step processor that is inserted in report processing pipeline to close data context after it has been fully
 * used, potentially releasing associated database resources
 */
public class CloseDataCursorProcessor implements StepProcessor {

    @Nonnull
    private final Step step;
    @Nonnull
    private final DataCursor dataCursor;

    CloseDataCursorProcessor(Step step, DataCursor dataCursor) {
        this.step = Objects.requireNonNull(step);
        this.dataCursor = Objects.requireNonNull(dataCursor);
    }

    @Override
    public ReportStep getStep() {
        return step;
    }

    @Override
    public Stream<StepProcessor> apply() {
        return Stream.of(this);
    }

    @Override
    public void execute() {
        dataCursor.close();
    }
}
