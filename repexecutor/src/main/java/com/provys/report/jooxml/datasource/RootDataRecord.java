package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Root data record is special kind of data record that supplies parameter values as data
 */
public class RootDataRecord implements DataRecord {

    private static final Logger LOG = LogManager.getLogger(RootDataRecord.class.getName());
    private final ReportContext reportContext;

    public RootDataRecord(ReportContext reportContext) {
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    private static Boolean convertStringToBoolean(String value) {
        if (value.equals("Y")) {
            return true;
        }
        if (value.equals("N")) {
            return false;
        }
        throw new RuntimeException("Invalid boolean value, Y or N expected, " + value + " found");
    }

    @Override
    public CellValue getValue(String columnName, @Nullable CellType prefType) {
        String value = reportContext.getParameterValue(columnName).orElse(null);
        final CellValueFactory cellValueFactory = reportContext.getCellValueFactory();
        CellValue result;
        if (prefType == null) {
            result = cellValueFactory.ofString(value);
        } else {
            switch (prefType) {
                case FORMULA:
                    LOG.warn("GetRecordValue: Request to retrieve data value made for cell type formula");
                    result = cellValueFactory.ofString(value);
                    break;
                case STRING:
                    result = cellValueFactory.ofString(value);
                    break;
                case NUMERIC:
                    if (value != null) {
                        final String fpRegex =
                                "[\\x00-\\x20]*" +                          // Optional leading "whitespace"
                                        "[+-]?" +                          // Optional sign character
                                        "(\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" + // digits and floating point
                                        "[\\x00-\\x20]*";                         // Optional trailing "whitespace"
                        if (Pattern.matches(fpRegex, value)) {
                            result = cellValueFactory.ofNumeric(Double.valueOf(value));
                        } else {
                            result = cellValueFactory.ofString(value);
                        }
                    } else {
                        result = cellValueFactory.ofNumeric(null);
                    }
                    break;
                case BOOLEAN:
                    if (value == null) {
                        result = cellValueFactory.ofBoolean(null);
                    } else if (value.equals("Y")) {
                        result = cellValueFactory.ofBoolean(true);
                    } else if (value.equals("N")) {
                        result = cellValueFactory.ofBoolean(false);
                    } else {
                        result = cellValueFactory.ofString(value);
                    }
                    break;
                case ERROR:
                    LOG.warn("GetRecordValue: Request to retrieve data value made for cell type error");
                    result = cellValueFactory.ofString(value);
                    break;
                default:
                    throw new RuntimeException("Unsupported preferred cell type " + prefType);
            }
        }
        return result;
    }

    @Override
    public CellValue getValue(String columnName) {
        return getValue(columnName, null);
    }
}
