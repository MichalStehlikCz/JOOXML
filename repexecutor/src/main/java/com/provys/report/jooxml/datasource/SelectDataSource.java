package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;

class SelectDataSource extends DataSourceAncestor {

    @Nonnull
    private final String selectStatement;

    SelectDataSource(ReportDataSource parent, String nameNm, String selectStatement) {
        super(parent, nameNm);
        if (selectStatement.isEmpty()) {
            throw new IllegalArgumentException("Select statement in select data source cannot be empty");
        }
        this.selectStatement = selectStatement;
    }

    /**
     * @return value of field selectStatement
     */
    @Nonnull
    String getSelectStatement() {
        return selectStatement;
    }

    @Nonnull
    @Override
    public DataContext getDataContext(ReportContext reportContext) {
        return new SelectDataContext(this, reportContext);
    }
}
