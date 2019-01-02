package com.provys.report.jooxml.repworkbook;

import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellValue;
import org.jetbrains.annotations.Nullable;

/**
 * Represents row in xlsx worksheet where report is being written to
 */
public interface RepRow {

    /**
     * Add cell at given position, set its value and properties
     *
     * @param colIndex    is column index cell should be added at, zero based
     * @param value is value to be set to new cell
     * @param properties are cell proeprties to be set (style, comment, hyperlink), nullable
     */
    void addCell(int colIndex, CellValue value, @Nullable CellProperties properties);

}