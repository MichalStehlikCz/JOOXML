package com.provys.jooxml.repexecutor;

import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

/**
 * Represents row in xlsx worksheet where report is being written to
 */
public interface RepWRow {

    /**
     * Add formula cell at given position.
     *
     * @param colIndex    is column index cell should be added at, zero based
     * @param cellFormula is formula that should be set to cell
     * @param styleIndex is index of cell style; we cannot pass CellStyle (as it belongs to different workbook), but
     *                    we might pass index, as cell styles in target workbook are the same as in template (given that
     *                    target workbook is copy of template and we do not manipulate with CellStyles); -1 if cell
     *                    style is not set for given cell
     * @param comment     is comment to be applied on cell
     * @param hyperlink   is hyperlink to be attached to cell
     */
    void addFormulaCell(int colIndex, String cellFormula, int styleIndex, Comment comment, Hyperlink hyperlink);

    /**
     * Add string cell at given position.
     *
     * @param colIndex  is column index cell should be added at, zero based
     * @param cellValue is value to be assigned to cell
     * @param styleIndex is index of cell style; we cannot pass CellStyle (as it belongs to different workbook), but
     *                    we might pass index, as cell styles in target workbook are the same as in template (given that
     *                    target workbook is copy of template and we do not manipulate with CellStyles); -1 if cell
     *                    style is not set for given cell
     * @param comment   is comment to be applied on cell
     * @param hyperlink is hyperlink to be attached to cell
     */
    void addStringCell(int colIndex, String cellValue, int styleIndex, Comment comment, Hyperlink hyperlink);

    /**
     * Add string cell at given position.
     *
     * @param colIndex  is column index cell should be added at, zero based
     * @param cellValue is value to be assigned to cell
     * @param styleIndex is index of cell style; we cannot pass CellStyle (as it belongs to different workbook), but
     *                    we might pass index, as cell styles in target workbook are the same as in template (given that
     *                    target workbook is copy of template and we do not manipulate with CellStyles); -1 if cell
     *                    style is not set for given cell
     * @param comment   is comment to be applied on cell
     * @param hyperlink is hyperlink to be attached to cell
     */
    void addNumericCell(int colIndex, double cellValue, int styleIndex, Comment comment, Hyperlink hyperlink);

    /**
     * Add string cell at given position.
     *
     * @param colIndex  is column index cell should be added at, zero based
     * @param cellValue is value to be assigned to cell
     * @param styleIndex is index of cell style; we cannot pass CellStyle (as it belongs to different workbook), but
     *                    we might pass index, as cell styles in target workbook are the same as in template (given that
     *                    target workbook is copy of template and we do not manipulate with CellStyles); -1 if cell
     *                    style is not set for given cell
     * @param comment   is comment to be applied on cell
     * @param hyperlink is hyperlink to be attached to cell
     */
    void addBooleanCell(int colIndex, boolean cellValue, int styleIndex, Comment comment, Hyperlink hyperlink);

    /**
     * Add string cell at given position.
     *
     * @param colIndex  is column index cell should be added at, zero based
     * @param cellValue is value to be assigned to cell
     * @param styleIndex is index of cell style; we cannot pass CellStyle (as it belongs to different workbook), but
     *                    we might pass index, as cell styles in target workbook are the same as in template (given that
     *                    target workbook is copy of template and we do not manipulate with CellStyles); -1 if cell
     *                    style is not set for given cell
     * @param comment   is comment to be applied on cell
     * @param hyperlink is hyperlink to be attached to cell
     */
    void addErrorCell(int colIndex, byte cellValue, int styleIndex, Comment comment, Hyperlink hyperlink);

}