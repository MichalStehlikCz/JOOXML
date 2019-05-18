package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;

public abstract class DataCursorAncestor<T extends DataContextAncestor> implements DataCursor {

    private final T dataContext;

    DataCursorAncestor(T dataContext) {
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
    T getDataContext() {
        return dataContext;
    }

}
