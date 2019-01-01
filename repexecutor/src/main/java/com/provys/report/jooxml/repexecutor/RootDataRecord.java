package com.provys.report.jooxml.repexecutor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Root data record is special kind of data record that supplies parameter values as data
 */
public class RootDataRecord implements DataRecord {

    private final ReportContext reportContext;

    public RootDataRecord(ReportContext reportContext) {
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    @Override
    public Optional<String> getStringValue(String columnName) {
        return reportContext.getParameterValue(columnName);
    }

    @Override
    public Optional<Double> getNumericValue(String columnName) {
        return reportContext.getParameterValue(columnName).map(Double::parseDouble);
    }

    @Override
    public Optional<LocalDateTime> getDateValue(String columnName) {
        return Optional.empty();
    }

    private static Boolean convertStringToBoolean(String value) {
        if (value.equals("Y")) {
            return true;
        }
        if (value.equals("N")) {
            return false;
        }
        throw new RuntimeException("Invalid boolean value, Y or N expected, " + value + " found");
    }
    @Override
    public Optional<Boolean> getBooleanValue(String columnName) {
        return reportContext.getParameterValue(columnName).map(RootDataRecord::convertStringToBoolean);
    }
}
