package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.util.Objects;

public class XmlChildDataSource extends DataSourceAncestor {

    @Nonnull
    private final String masterTag;
    @Nonnull
    private final String rowTag;

    XmlChildDataSource(ReportDataSource parent, String nameNm, String masterTag, String rowTag) {
        super(parent, nameNm);
        this.masterTag = Objects.requireNonNull(masterTag);
        this.rowTag = Objects.requireNonNull(rowTag);
    }

    /**
     * @return value of field masterTag
     */
    @Nonnull
    String getMasterTag() {
        return masterTag;
    }

    /**
     * @return value of field rowTag
     */
    @Nonnull
    String getRowTag() {
        return rowTag;
    }

    @Nonnull
    @Override
    public DataContext getDataContext(ReportContext reportContext) {
        return new XmlChildDataContext(this, reportContext);
    }
}
