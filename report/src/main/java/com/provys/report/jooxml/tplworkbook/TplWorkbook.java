package com.provys.report.jooxml.tplworkbook;

import java.io.Closeable;

/**
 * Interface defines functionality, required from template workbook by JOOXML report executor.
 */
public interface TplWorkbook extends Closeable, Iterable<TplSheet> {

    /**
     * Get sheet with given index.
     *
     * @return sheet on given position, indexed from 0
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    TplSheet getSheetAt(int i);

    /**
     * Get main (first) sheet of given workbook. Used when sheet is not explicitly specified
     *
     * @return first sheet of given workbook
     */
    TplSheet getSheet();

}
