package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * RootDataSource is data source, that has internal name ROOT and no parent. It returns data stream that has
 * single row and contains values of report parameters
 */
class RootDataSource extends DataSourceRootAncestor {

    static final String NAME_NM = "ROOT";

    /**
     * Creates root data source
     */
    RootDataSource() {}

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
        return NAME_NM;
    }

    @Nonnull
    @Override
    public DataContext getDataContext(ReportContext reportContext) {
        return new RootDataContext(this, reportContext);
    }
}
