package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;

import javax.annotation.Nonnull;

class OraSelectDataSource extends DataSourceAncestor {

    @Nonnull
    private final String selectStatement;

    OraSelectDataSource(ReportDataSource parent, String nameNm, String selectStatement) {
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
    public String getSelectStatement() {
        return selectStatement;
    }

    @Nonnull
    @Override
    public DataContext getDataContext() {
        return null;
    }
}
