package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;

abstract class DataRecordRowAncestor extends DataRecordAncestor {

    private final long rowNumber;

    DataRecordRowAncestor(ReportContext reportContext, long rowNumber) {
        super(reportContext);
        this.rowNumber = rowNumber;
    }

    @Override
    public long getRowNumber() {
        return rowNumber;
    }
}
