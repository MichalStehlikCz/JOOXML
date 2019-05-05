package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Cursor;
import org.jooq.Param;
import org.jooq.ResultQuery;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @Override
    public void prepare() {
        if (query == null) {
            parse();
        }
    }

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

    @Override
    public synchronized void close() {
        if (query != null) {
            query.close();
            query = null;
        }
    }
}
