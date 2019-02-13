package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;

/**
 * Factory class that produces various data sources available
 */
@ApplicationScoped
public class DataSourceFactory {

    /**
     * @return root data source (has internal name ROOT and no parent)
     */
    @Nonnull
    public ReportDataSource getRootDataSource() {
        return new RootDataSource();
    }

}
