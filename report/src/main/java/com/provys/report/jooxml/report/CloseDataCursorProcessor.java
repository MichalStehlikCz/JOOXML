package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class is step processor that is inserted in report processing pipeline to close data context after it has been fully
 * used, potentially releasing associated database resources
 */
public class CloseDataCursorProcessor implements StepProcessor {

    private static final Logger LOG = LogManager.getLogger(CloseDataCursorProcessor.class);

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
        LOG.trace("Execute CloseDataCursorProcessor {}", dataCursor);
        dataCursor.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseDataCursorProcessor that = (CloseDataCursorProcessor) o;
        if (!getStep().equals(that.getStep())) return false;
        return dataCursor.equals(that.dataCursor);
    }

    @Override
    public int hashCode() {
        int result = getStep().hashCode();
        result = 31 * result + dataCursor.hashCode();
        return result;
    }
}
