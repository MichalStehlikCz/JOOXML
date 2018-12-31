package com.provys.jooxml.repexecutor;

public interface DataRecord {
    String getStringValue(String columnName);
    double getNumericValue(String columnName);
    boolean getBooleanValue(String columnName);
}
