package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;

import javax.annotation.Nonnull;

/**
 * Keeps resources needed to retrieve data for ReportDataSource.
 * Represents ReportDataSource in context of report execution, represented by ReportContext. DataContext is created and
 * its lifecycle is fully controlled by ReportContext. It holds resources needed to access data - like parsed prepared
 * statement - and can return stream of DataRecord objects.
 * Constructor should not be too slow, preparation should be deferred to prepare method and prepare method must be
 * called before data context usage
 */
public interface DataContext extends AutoCloseable {

    /**
     * @return data source this context was created for
     */
    @Nonnull
    ReportDataSource getDataSource();

    /**
     * Prepare associated prepared statement etc.
     * Data context should track its state and repeated execution of prepare method should do nothing.
     */
    void prepare();

    /**
     * Retrieves data from data context. Supplies master data that can be bound or otherwise used. DataContext keeps
     * track of open cursors and closes remaining ones on its closure
     *
     * @param master is master data record
     * @return stream of data records read from this data source
     */
    DataCursor execute(DataRecord master);

    /**
     * Close resources associated with data context. Unlike AutoCloseable, this method close does not throw Exception.
     * Apart from context itself, it also closes any cursors retrieved from this data context that has not been closed
     * yet.
     */
    @Override
    void close();
}
