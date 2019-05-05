package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Common ancestor for DataContext classes. Holds associated {@code ReportDataSource} and gives access to it. Also holds
 * list of opened DataCursors and closes them if they were not closed on their own
 *
 * @param <T> is type of data source given context can reference
 */
abstract class DataContextAncestor<T extends ReportDataSource> implements DataContext {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(DataContextAncestor.class);

    @Nonnull
    private final T dataSource;
    @Nonnull
    private final ReportContext reportContext;
    @Nonnull
    // Holds cursor that has been fetched from this data source and not closed yet. Default size is 1 as usually, first
    // cursor is closed before next one is created
    private final List<DataCursor> openedCursors = new ArrayList<>(1);

    DataContextAncestor(T dataSource, ReportContext reportContext) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    @Nonnull
    @Override
    public T getDataSource() {
        return dataSource;
    }

    /**
     * @return value of field reportContext
     */
    @Nonnull
    ReportContext getReportContext() {
        return reportContext;
    }

    @Override
    public void prepare() {
        // by default, data context does not need any preparation
    }

    /**
     * Used to add cursor to list of opened cursors, called from cursor constructor
     *
     * @param cursor is cursor to be added to list of opened cursors
     */
    void cursorOpened(DataCursor cursor) {
        openedCursors.add(cursor);
    }

    /**
     * Called when cursor is closed, removes it from array of opened cursors
     *
     * @param cursor is cursor that has been closed and should be removed from list of opened cursors
     */
    void cursorClosed(DataCursor cursor) {
        if (!openedCursors.remove(cursor)) {
            LOG.debug("Trying to remove cursor from opened cursors, but cursor is not present (probably already closed)");
        }
    }

    @Override
    public void close() {
        // close all not-yet-closed cursors; close removes item from array, thus we have to go from last item...
        // no need for some special optimisation as usually, list is empty
        for (var i = openedCursors.size()-1; i >= 0; i--) {
            openedCursors.get(i).close();
        }
    }

}
