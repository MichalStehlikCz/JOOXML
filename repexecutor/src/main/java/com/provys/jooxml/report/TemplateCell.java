package com.provys.jooxml.report;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

public interface TemplateCell {

    /**
     * @return row index of given cell; rows are indexed from 0, so this is one lower than row number visible in Excel
     */
    int getRowIndex();

    /**
     * @return column index; columns are indexed from 0
     */
    int getColumnIndex();

    /**
     * @return get type of cell (string, numeric, boolean, formula); blank cells are not supported
     */
    CellType getCellType();

    /**
     * @return string value, representing formula present in given cell
     */
    String getCellFormula();

    /**
     * @return value for cell with type string
     */
    String getStringCellValue();

    /**
     * @return value for cell with type numeric
     */
    double getNumericCellValue();

    /**
     * @return return value for cell with type boolean
     */
    boolean getBooleanCellValue();

    /**
     * @return return value for cell with type error
     */
    byte getErrorCellValue();

    /**
     * @return comment attached to given cell
     */
    Comment getCellComment();

    /**
     * @return hyperlink attached to given cell
     */
    Hyperlink getHyperlink();

}
