package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.DataRecord;

import java.util.stream.Stream;

/**
 * DataCursor is used to access data from
 */
public interface DataCursor extends AutoCloseable {

    /**
     * @return stream of data associated with cursor. Note that cursor holds single stream, it is not possible to call
     * this method multiple times to navigate data repeatedly. Also note that steam is closed once cursor is closed
     */
    Stream<DataRecord> getData();

    /**
     * Close resources associated with data context. Unlike AutoCloseable, this method close does not throw Exception.
     * Note that cursor can only be closed once all data has been read. While stream is not accessible, retrieved data
     * should still be accessible once cursor is closed
     */
    @Override
    void close();
}
