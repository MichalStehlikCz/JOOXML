package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Record;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class OraDataRecord extends DataRecordAncestor {

    private final int rowNumber;
    Record data;

    OraDataRecord(ReportContext reportContext, int rowNumber, Record data) {
        super(reportContext);
        this.rowNumber = rowNumber;
        this.data = data;
    }


    @Override
    public int getRowNumber() {
        return rowNumber;
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        return Optional.ofNullable(data.getValue(columnName));
    }
}
