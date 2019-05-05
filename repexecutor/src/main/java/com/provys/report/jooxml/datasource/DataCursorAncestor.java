package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;

public abstract class DataCursorAncestor implements DataCursor {

    private final DataContextAncestor dataContext;

    DataCursorAncestor(DataContextAncestor dataContext) {
        this.dataContext = dataContext;
        dataContext.cursorOpened(this);
    }

    @Override
    public void close() {
        dataContext.cursorClosed(this);
    }

    /**
     * @return DataContext this cursor has been created from
     */
    DataContextAncestor getDataContext() {
        return dataContext;
    }

}
