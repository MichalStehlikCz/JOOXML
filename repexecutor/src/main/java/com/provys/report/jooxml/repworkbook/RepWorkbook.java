package com.provys.report.jooxml.repworkbook;

import com.provys.report.jooxml.repexecutor.ExecRegion;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface represents target xlsx workbook where report content is pushed.
 */
public interface RepWorkbook extends Closeable {

    /**
     * Get default (first) sheet.
     *
     * @return sheet at workbook with position 0
     */
    RepSheet getSheet();

    /**
     * Get and return sheet at specified position
     *
     * @param index is sheet index (zero based)
     * @return sheet at specified position
     */
    RepSheet getSheetAt(int index);

    /**
     * Write workbook to supplied output stream.
     *
     * @param stream is outpus stream workbook should be written to
     * @param execRegion is map of regions written to worksheet. Used to resolve formula cell references
     * @throws IOException if any problem happens writing to output stream
     */
    void write(OutputStream stream, ExecRegion execRegion) throws IOException;
}
