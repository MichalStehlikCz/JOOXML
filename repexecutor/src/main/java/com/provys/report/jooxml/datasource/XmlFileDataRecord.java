package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class XmlFileDataRecord extends DataRecordAncestor {

    XmlFileDataRecord(ReportContext reportContext) {
        super(reportContext);
    }

    @Override
    public long getRowNumber() {
        return 0;
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        return Optional.empty();
    }
}
