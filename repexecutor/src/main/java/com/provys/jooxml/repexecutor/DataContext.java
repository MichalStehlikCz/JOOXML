package com.provys.jooxml.repexecutor;

/**
 * Represents data context associated with RegionProcessor.
 * Binds RegionProcessor to DataSource, holds resources needed to access data and can return actual data stream.
 */
public interface DataContext extends AutoCloseable {
    ReportDataSource getDataSource();
    DataStream execute();
}
