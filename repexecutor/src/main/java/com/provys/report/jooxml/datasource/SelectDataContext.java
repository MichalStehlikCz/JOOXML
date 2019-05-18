package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Param;
import org.jooq.ResultQuery;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Select data context represents select data sorce and its state during report execution.
 * On prepare, it parses select statement held by select data source sing JOOQ parser. Select statement is held parsed
 * and is closed on close of this context.
 * On execute, query is executed and resulting cursor is returned
 */
public class SelectDataContext extends DataContextAncestor<SelectDataSource> {

    @Nullable
    private ResultQuery<?> query;

    SelectDataContext(SelectDataSource dataSource, ReportContext reportContext) {
        super(dataSource, reportContext);
    }

    /**
     * Create prepared statement and parse select text
     */
    private synchronized void parse() {
        if (query == null) {
            query = getReportContext().getDslContext().parser().parseResultQuery(getDataSource().getSelectStatement());
        }
    }

    /**
     * Parse associated statement
     */
    @Override
    public void prepare() {
        if (query == null) {
            parse();
        }
    }

    /**
     * Execute associated statement. Uses lazy fetching, execute returns cursor that is opened against database.
     *
     * @param master is master (parent) data record, used to populate bind variables in statement
     * @return cursor that holds database cursor that can be used to fetch results
     */
    @Override
    public DataCursor execute(DataRecord master) {
        if (query == null) {
            throw new IllegalStateException("Cannot execute data context - context not prepared");
        }
        Map<String, Param<?>> params = query.getParams();
        for (Param<?> param : params.values()) {
            var paramName = param.getParamName();
            if (paramName != null) {
                query.bind(paramName, master.getValue(paramName.toUpperCase(), null).orElse(null));
            }
        }
        return new SelectDataCursor(this, query.fetchLazy());
    }

    /**
     * Close parsed statement. Note that all cursors produced by execute should be closed first.
     */
    @Override
    public synchronized void close() {
        if (query != null) {
            query.close();
            query = null;
        }
    }
}
