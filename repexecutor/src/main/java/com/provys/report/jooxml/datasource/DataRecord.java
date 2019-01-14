package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nullable;

public interface DataRecord {
    /**
     * Get value of specified column from data record.
     *
     * @param columnName is name of column
     * @param prefType is preferred type of column value to be returned; method can return value of different type
     *                rather than  fail if value cannot be converted to required type
     * @return retrieved value; it can be potentially empty cell value if column is not found or has empty value
     */
    CellValue getValue(String columnName, @Nullable CellType prefType);
    CellValue getValue(String columnName);
}
