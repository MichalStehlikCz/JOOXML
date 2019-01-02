package com.provys.report.jooxml.tplworkbook;

import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import java.util.Optional;

public interface TplCell {

    /**
     * @return row index of given cell; rows are indexed from 0, so this is one lower than row number visible in Excel
     */
    int getRowIndex();

    /**
     * @return column index; columns are indexed from 0
     */
    int getColIndex();

    /**
     * @return type of cell
     */
    CellType getCellType();

    /**
     * @return value present in given cell
     */
    CellValue getCellValue();

    /**
     * @return proeprties (style, comment, hyperlink) of given cell
     */
    Optional<CellProperties> getCellProperties();

}
