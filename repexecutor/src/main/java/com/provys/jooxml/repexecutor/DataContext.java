package com.provys.jooxml.repexecutor;

import java.util.stream.Stream;

/**
 * Represents data context associated with RegionProcessor.
 * Binds RegionProcessor to DataSource, holds resources needed to access data and can return actual data stream.
 */
public interface DataContext extends AutoCloseable {
    ReportDataSource getDataSource();

    /**
     * Prepare data context. For database data sources, it means create PreparedStatement that will be then executed on
     * each invocation of execute. Does nothing for data sources that do not need prior preparation.
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
