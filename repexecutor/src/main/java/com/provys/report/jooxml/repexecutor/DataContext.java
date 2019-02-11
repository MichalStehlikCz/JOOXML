package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Represents data context associated with RegionProcessor.
 * Binds RegionProcessor to DataSource, holds resources needed to access data and can return actual data stream.
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
     * Retrieves data from data context. Supplies master data that can be bound or otherwise used.
     *
     * @param master is master data record
     * @return stream of data records read from this data source; note that given it is stream, data context cannot be
     * safely reused util all data are read
     */
    Stream<DataRecord> execute(DataRecord master);
}
