package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;

import java.util.Optional;

/**
 * Data source describes how to access report data.
 * Particular implementations might have embedded data supplied via constructor, point to file or database query.
 */
public interface ReportDataSource {
    /**
     * @return parent data source
     */
    Optional<ReportDataSource> getParent();

    /**
     * @return internal name of data source
     */
    String getNameNm();

    /**
     * @return DataContext created based on given data source
     */
    DataContext getDataContext();
}
