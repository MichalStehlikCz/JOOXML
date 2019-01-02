package com.provys.report.jooxml.repworkbook;

import com.provys.report.jooxml.repexecutor.RowProperties;
import org.jetbrains.annotations.Nullable;

/**
 * Interface represents sheet in xlsx workbook where report writes its content
 */
public interface RepSheet {

    /**
     * Return row with given row index. If row does not exist yet, create it.
     *
     * @param rowIndex is index of row to be retrieved. RowImpl indices are zero based, e.g. it is one lower than actual row
     *                number in Excel
     * @return row with give index
     */
    RepRow getOrCreateRow(int rowIndex);

    /**
     * Create row on given index.
     *
     * @param rowIndex is index of the row to be created. RowImpl indices are zero based, e.g. it is one lower than actual
     *                 row number in Excel
     * @param rowProperties are properties to be assigned to new row
     */
    RepRow createRow(int rowIndex, @Nullable RowProperties rowProperties);
}
