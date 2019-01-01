package com.provys.report.jooxml.repexecutor;

/**
 * Interface represents sheet in xlsx workbook where report writes its content
 */
public interface RepWSheet {

    /**
     * Return row with given row index. If row does not exist yet, create it.
     *
     * @param rowIndex is index of row to be retrieved. RowImpl indices are zero based, e.g. it is one lower than actual row
     *                number in Excel
     * @return row with give index
     */
    RepWRow getOrCreateRow(int rowIndex);

    /**
     * Create row on given index.
     *
     * @param rowIndex is index of the row to be created. RowImpl indices are zero based, e.g. it is one lower than actual
     *                 row number in Excel
     * @param rowProperties are properties to be assigned to new row
     */
    RepWRow createRow(int rowIndex, RowProperties rowProperties);
}
