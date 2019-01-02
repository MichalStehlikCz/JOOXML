package com.provys.report.jooxml.datasource;

/**
 * Factory class that produces various data sources available
 */
public class DataSourceFactory {

    /**
     * @return root data source (has internal name ROOT and no parent)
     */
    public static ReportDataSource getRootDataSource() {
        return RootDataSource.getInstance();
    }

}
