package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * RootDataSource is singleton - data source, that has internal name ROOT and no parent. It returns data stream that has
 * single row and contains values of parameters
 */
class RootDataSource implements ReportDataSource {

    @Nonnull
    private static final RootDataSource instance = new RootDataSource();

    /**
     * @return the only instance of root data executor there is
     */
    @Nonnull
    public static RootDataSource getInstance() {
        return instance;
    }

    /**
     * Creates root data source
     */
    private RootDataSource() {}

    /**
     * Root data source has no parent
     *
     * @return empty optional (as root data source has no parent)
     */
    @Nonnull
    @Override
    public Optional<ReportDataSource> getParent() {
        return Optional.empty();
    }

    /**
     * Internal name of root data source is always ROOT.
     *
     * @return string ROOT
     */
    @Nonnull
    @Override
    public String getNameNm() {
        return "ROOT";
    }

    @Nonnull
    @Override
    public DataContext getDataContext(ReportContext reportContext) {
        return new RootDataContext(this, reportContext);
    }
}
