package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.DataRecord;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Optional;

public interface AreaCell {

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
     * Get effective value of given cell. If column binding is present in cell, retrieve value from supplied data.
     * Otherwise, use cell value. Should only be used for String or Blank cells
     *
     * @param data is data record used to evaluate value from binding
     * @return value for cell with type string or blank
     */
    String getStringEffectiveValue(DataRecord data);

    /**
     * Get effective value of given cell. If column binding is present in cell, retrieve value from supplied data.
     * Otherwise, use cell value. Should only be used for Numeric cells
     *
     * @param data is data record used to evaluate value from binding
     * @return value for cell with type numeric
     */
    double getNumericEffectiveValue(DataRecord data);

    /**
     * Get effective value of given cell. If column binding is present in cell, retrieve value from supplied data.
     * Otherwise, use cell value. Should only be used for Boolean cells
     *
     * @param data is data record used to evaluate value from binding
     * @return value for cell with type boolean
     */
    boolean getBooleanEffectiveValue(DataRecord data);

    /**
     * @return index of style associated with cell, -1 if no style is applied
     */
    int getCellStyleIndex();

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
