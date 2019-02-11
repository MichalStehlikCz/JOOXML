package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Root data record is special kind of data record that supplies parameter values as data
 */
public class RootDataRecord extends DataRecordAncestor {

    private static final Logger LOG = LogManager.getLogger(RootDataRecord.class.getName());

    public RootDataRecord(ReportContext reportContext) {
        super(reportContext);
    }

    @Override
    public int getRowNumber() {
        return 0;
    }

    @Nonnull
    private static Boolean convertStringToBoolean(String value) {
        if (value.equals("Y")) {
            return true;
        }
        if (value.equals("N")) {
            return false;
        }
        throw new RuntimeException("Invalid boolean value, Y or N expected, " + value + " found");
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String paramName, @Nullable Class<?> prefClass) {
        String value = getReportContext().getParameterValue(paramName).orElse(null);
        Object result;
        if (value == null){
            result = null;
        } else if (prefClass == null) {
            result = value;
        } else {
            switch (prefClass.getName()) {
                case "String":
                    result = value;
                    break;
                case "Double":
                    final String fpRegex =
                                "[\\x00-\\x20]*" +                          // Optional leading "whitespace"
                                        "[+-]?" +                          // Optional sign character
                                        "(\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" + // digits and floating point
                                        "[\\x00-\\x20]*";                         // Optional trailing "whitespace"
                        if (Pattern.matches(fpRegex, value)) {
                            result = Double.valueOf(value);
                        } else {
                            result = value;
                        }
                    break;
                case "Boolean":
                    if (value.equals("Y")) {
                        result = Boolean.TRUE;
                    } else if (value.equals("N")) {
                        result = Boolean.FALSE;
                    } else {
                        result = value;
                    }
                    break;
                default:
                    throw new RuntimeException("Unsupported preferred result class " + prefClass.getName());
            }
        }
        return Optional.ofNullable(result);
    }

}
