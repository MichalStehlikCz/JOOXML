package com.provys.jooxml.repexecutor;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Optional;

public interface ReportRegionCell {

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
     * @return hyperlink attached to given cell; it might be null, but we do not use Optional here as null is accepted
     * by
     */
    Hyperlink getHyperlink();

    /**
     * @return source field if cell has data binding
     */
    Optional<String> getBindColumn();
}