package com.provys.jooxml.datasource;

import com.provys.jooxml.repexecutor.ReportDataSource;

import java.util.stream.Stream;

public interface DataStream extends Stream<DataRecord> {
    /**
     * @return ReportDataSource this stream was created from
     */
    public ReportDataSource getDataSource();
}
