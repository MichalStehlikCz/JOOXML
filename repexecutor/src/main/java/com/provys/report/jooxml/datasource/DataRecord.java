package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface DataRecord {

    /**
     * @return row number of record within data context
     */
    long getRowNumber();

    /**
     * Get value of specified column
     *
     * @param columnName is name of column to be bound
     * @param prefType is preferred type of column value to be returned; method can return value of different type
     *                rather than  fail if value cannot be converted to required type
     * @return value to be bound; log warning and return empty optional if value is not successfully evaluated
     */
    @Nonnull
    Optional<Object> getValue(String columnName, @Nullable CellType prefType);

    /**
     * Get value of specified column from data record.
     *
     * @param columnName is name of column
     * @param prefType is preferred type of column value to be returned; method can return value of different type
     *                rather than  fail if value cannot be converted to required type
     * @return retrieved value; it can be potentially empty cell value if column is not found or has empty value
     */
    @Nonnull
    CellValue getCellValue(String columnName, @Nullable CellType prefType);

    /**
     * Get value of specified column from data record; variant without preferred type
     *
     * @param columnName is name of column
     * @return retrieved value; it can be potentially empty cell value if column is not found or has empty value
     */
    @Nonnull
    CellValue getCellValue(String columnName);

}
