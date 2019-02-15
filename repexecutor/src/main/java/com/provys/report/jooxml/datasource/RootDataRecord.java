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
    public long getRowNumber() {
        return 0L;
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
                    switch (value) {
                        case "Y":
                            result = Boolean.TRUE;
                            break;
                        case "N":
                            result = Boolean.FALSE;
                            break;
                        default:
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
