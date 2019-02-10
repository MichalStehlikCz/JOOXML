package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Param;
import org.jooq.Query;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public class OraSelectDataContext extends DataContextAncestor<OraSelectDataSource> {

    @Nullable
    private Query query;

    OraSelectDataContext(OraSelectDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Create prepared statement and parse select text
     *
     * @param reportContext is report context in which this data context will be valid. Used to retrieve connection to
     *                     PROVYS database.
     */
    private synchronized void parse(ReportContext reportContext) {
        if (query == null) {
            query = reportContext.getDslContext().parser().parseQuery(getDataSource().getSelectStatement());
        }
    }

    @Override
    public void prepare(ReportContext reportContext) {
        if (query == null) {
            parse(reportContext);
        }
    }

    @Override
    public Stream<DataRecord> execute(DataRecord master) {
        if (query == null) {
            throw new IllegalStateException("Cannot execute data context - context not prepared");
        }
        Map<String, Param<?>> params = query.getParams();
        for (Param<?> param : params.values()) {
            if (!master.bindToQuery(query, param.getName())) {
                query.bind(param.getName(), null);
            }
        }
    }

    @Override
    public synchronized void close() throws Exception {
        if (query != null) {
            query.close();
            query = null;
        }
    }
}
