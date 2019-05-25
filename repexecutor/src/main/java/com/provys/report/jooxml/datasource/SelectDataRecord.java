package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.jooq.Record;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

class SelectDataRecord extends DataRecordRowAncestor {

    private final Record data;

    SelectDataRecord(ReportContext reportContext, long rowNumber, Record data) {
        super(reportContext, rowNumber);
        this.data = data;
    }


    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        return Optional.ofNullable(data.getValue(columnName));
    }
}
