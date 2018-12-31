package com.provys.jooxml.repexecutor;

import com.provys.jooxml.repexecutor.DataRecord;
import com.provys.jooxml.repexecutor.ReportContext;

import java.util.Objects;

/**
 * Root data record is special kind of data record that supplies parameter values as data
 */
public class RootDataRecord implements DataRecord {

    private final ReportContext reportContext;

    public RootDataRecord(ReportContext reportContext) {
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    @Override
    public String getStringValue(String columnName) {
        return reportContext.getParameterValue(columnName);
    }

    @Override
    public double getNumericValue(String columnName) {
        return Double.parseDouble(reportContext.getParameterValue(columnName));
    }

    @Override
    public boolean getBooleanValue(String columnName) {
        String value = reportContext.getParameterValue(columnName);
        if (value.equals("Y")) {
            return true;
        }
        if (value.equals("N")) {
            return false;
        }
        throw new RuntimeException("Invalid boolean value, Y or N expected, " + value + " found");
    }
}
