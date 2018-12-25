package com.provys.jooxml.datasource;

import com.provys.jooxml.repexecutor.ReportDataSource;

import java.util.Optional;

/**
 * RootDataSource is singleton - data source, that has internal name ROOT and no parent. It returns data stream that has
 * single row and contains values of parameters
 */
class RootDataSource implements ReportDataSource {

    private static final RootDataSource instance = new RootDataSource();

    /**
     * @return the only instance of root data executor there is
     */
    public static RootDataSource getInstance() {
        return instance;
    }

    /**
     * Creates root data source
     */
    private RootDataSource() {};

    /**
     * Root data source has no parent
     *
     * @return empty optional (as root data source has no parent)
     */
    @Override
    public Optional<ReportDataSource> getParent() {
        return Optional.empty();
    }

    /**
     * Internal name of root data source is always ROOT.
     *
     * @return string ROOT
     */
    @Override
    public String getNameNm() {
        return "ROOT";
    }
}
