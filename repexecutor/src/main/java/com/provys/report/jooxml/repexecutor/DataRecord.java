package com.provys.report.jooxml.repexecutor;

import java.util.Optional;
import java.time.LocalDateTime;

public interface DataRecord {
    Optional<String> getStringValue(String columnName);
    Optional<Double> getNumericValue(String columnName);
    Optional<LocalDateTime> getDateValue(String columnName);
    Optional<Boolean> getBooleanValue(String columnName);
}
