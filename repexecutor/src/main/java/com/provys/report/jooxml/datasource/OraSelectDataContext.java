package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import oracle.jdbc.OracleParameterMetaData;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class OraSelectDataContext extends DataContextAncestor<OraSelectDataSource> {

    @Nullable
    private PreparedStatement preparedStatement;

    OraSelectDataContext(OraSelectDataSource dataSource) {
        super(dataSource);
    }

    private static void parseBinds(PreparedStatement preparedStatement) {
        try {
            OracleParameterMetaData metaData = preparedStatement.getParameterMetaData().
                    unwrap(OracleParameterMetaData.class);
            for (int i = 0; i < metaData.getParameterCount(); i++) {
                metaData.
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot parse statement parameters", e);
        }
    }
    /**
     * Create prepared statement and parse select text
     *
     * @param reportContext is report context in which this data context will be valid. Used to retrieve connection to
     *                     PROVYS database.
     */
    private synchronized void prepareStatement(ReportContext reportContext) {
        PreparedStatement genericPreparedStatement;
        try {
            preparedStatement = reportContext.getConnection().prepareStatement(getDataSource().getSelectStatement());
        } catch (SQLException e) {
            throw new RuntimeException("Cannot parse select statement: " + getDataSource().getSelectStatement(), e);
        }
        parseBinds();
    }

    @Override
    public void prepare(ReportContext reportContext) {
        if (preparedStatement == null) {
            prepareStatement(reportContext);
        }
    }

    @Override
    public Stream<DataRecord> execute(DataRecord master) {
        if (preparedStatement == null) {
            throw new IllegalStateException("Cannot execute data context - context not prepared");
        }
        try {
            preparedStatement.clearParameters();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot clear parameters in prepared statement", e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (preparedStatement != null) {
            preparedStatement.close();
            preparedStatement = null;
        }
    }
}
