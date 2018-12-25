package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegionCell;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import org.apache.poi.ss.util.CellReference;

import java.util.*;

public class ReportRegionRowImpl implements ReportRegionRow {

    final private int rowIndex;
    final private List<ReportRegionCell> cells;

    ReportRegionRowImpl(int rowIndex, List<ReportRegionCell> cells) {
        this.rowIndex = rowIndex;
        this.cells = cells;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public List<ReportRegionCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    @Override
    public Iterator<ReportRegionCell> iterator() {
        return Collections.unmodifiableList(cells).iterator();
    }

}
