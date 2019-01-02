package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import java.util.Optional;

public interface AreaCell {

    /**
     * @return column index; columns are indexed from 0
     */
    int getColIndex();

    /**
     * @return get type of cell (string, numeric, boolean, formula); blank cells are not supported
     */
    CellType getCellType();

    /**
     * @return value present in given cell
     */
    Optional<CellValue> getCellValue();

    /**
     * Get effective value of given cell. If column binding is present in cell, retrieve value from supplied data.
     * Otherwise, use cell value.
     *
     * @param data is data record used to evaluate value from binding
     * @return value of given cell
     */
    CellValue getEffectiveValue(DataRecord data);

    /**
     * @return properties associated with given cell
     */
    Optional<CellProperties> getProperties();

    /**
     * @return source field if cell has data binding
     */
    Optional<String> getBindColumn();
}
