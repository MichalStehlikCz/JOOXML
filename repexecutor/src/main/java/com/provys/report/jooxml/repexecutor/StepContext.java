package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.DataRecord;

import java.util.Objects;

/**
 * Provides context for step execution.
 * As much as it does not allow modification of its fields, note that these fields themselves are modifiable and hold
 * context of execution of report
 */
public class StepContext {
    private final DataRecord data;
    private final ContextCoordinates coordinates;
    private final ReportContext reportContext;

    /**
     * Creates step context from supplied data and coordinates context. Keeps reference to context coordinates, as these
     * might be intentionally shared between step contexts
     *
     * @param reportContext is report context this steps executed in. Holds reference to target workbook
     * @param data is data record that is used for current step
     * @param coordinates are offsets where step should be placed, and that step should move further
     */
    public StepContext(ReportContext reportContext, DataRecord data, ContextCoordinates coordinates) {
        this.reportContext = Objects.requireNonNull(reportContext);
        this.data = Objects.requireNonNull(data);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates step context with most properties same, but data replaced with supplied data
     */
    public StepContext cloneWithReplaceData(DataRecord data) {
        return new StepContext(getReportContext(), Objects.requireNonNull(data), getCoordinates());
    }

    /**
     * @return data record
     */
    public DataRecord getData() {
        return data;
    }

    /**
     * @return coordinates used in this context
     */
    public ContextCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return report context this step is executed in
     */
    public ReportContext getReportContext() {
        return reportContext;
    }
}
