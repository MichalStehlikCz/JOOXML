package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

/**
 * Data source describes how to access report data.
 * Particular implementations might have embedded data supplied via constructor, point to file or database query.
 */
public interface ReportDataSource {
    /**
     * @return parent data source
     */
    @Nonnull
    Optional<ReportDataSource> getParent();

    /**
     * @return read-only collection of child data sources
     */
    @Nonnull
    Collection<ReportDataSource> getChildren();

    /**
     * @return internal name of data source
     */
    @Nonnull
    String getNameNm();

    /**
     * Retrieve data context for execution of given dataset within report execution.
     * Note that direct use of this method is not recommended, as it bypasses caching of data context in report context;
     * callers should call ReportContext's getDataContext method and it will invoke this method if data context is not
     * opened for given report execution / data source yet.
     *
     * @param reportContext is report context defining scope of given data context; used to supply factory methods etc.
     * @return DataContext created based on given data source
     */
    @Nonnull
    DataContext getDataContext(ReportContext reportContext);
}
