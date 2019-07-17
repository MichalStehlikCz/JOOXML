package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

abstract class DataRecordAncestor implements DataRecord {

    private static final Logger LOG = LogManager.getLogger(RootDataRecord.class.getName());

    @Nonnull
    private final ReportContext reportContext;

    DataRecordAncestor(ReportContext reportContext) {
        this.reportContext = Objects.requireNonNull(reportContext);
    }

    /**
     * @return value of field reportContext
     */
    @Nonnull
    ReportContext getReportContext() {
        return reportContext;
    }

    @Nonnull
    @Override
    public CellValue getCellValue(String columnName, @Nullable CellType prefType) {
        CellType effPrefType = prefType;
        if (effPrefType == CellType.FORMULA) {
            LOG.warn("GetRecordValue: Request to retrieve data value made for cell type formula");
            effPrefType = null;
        } else if (effPrefType == CellType.ERROR) {
            LOG.warn("GetRecordValue: Request to retrieve data value made for cell type error");
            effPrefType = null;
        }
        Optional<Object> optValue = getValue(columnName, prefType);
        final CellValueFactory cellValueFactory = reportContext.getCellValueFactory();
        CellValue result;
        if (optValue.isEmpty()) {
            if (effPrefType == null) {
                result = cellValueFactory.getBlank();
            } else {
                switch (effPrefType) {
                    case STRING:
                        result = cellValueFactory.ofString(null);
                        break;
                    case NUMERIC:
                        result = cellValueFactory.ofNumeric(null);
                        break;
                    case BOOLEAN:
                        result = cellValueFactory.ofBoolean(null);
                        break;
                    default:
                        result = cellValueFactory.getBlank();
                }
            }
        } else {
            @SuppressWarnings("squid:S3655") // sonar doesn't recognise isEmpty yet...
            Object value = optValue.get();
            if (effPrefType == CellType.STRING) {
                result = cellValueFactory.ofString(value.toString());
            } else {
                switch (value.getClass().getName()) {
                    case "java.lang.String":
                        result = cellValueFactory.ofString((String) value);
                        break;
                    case "java.lang.Double":
                        result = cellValueFactory.ofNumeric((Double) value);
                        break;
                    case "java.lang.Integer":
                        result = cellValueFactory.ofNumeric(((Integer) value).doubleValue());
                        break;
                    case "java.math.BigDecimal":
                        result = cellValueFactory.ofNumeric(((BigDecimal) value).doubleValue());
                        break;
                    case "java.math.BigInteger":
                        result = cellValueFactory.ofNumeric(((BigInteger) value).doubleValue());
                        break;
                    case "java.lang.Boolean":
                        result = cellValueFactory.ofBoolean((Boolean) value);
                        break;
                    default:
                        result = cellValueFactory.ofString(value.toString());
                }
            }
        }
        return result;
    }

    @Nonnull
    @Override
    public CellValue getCellValue(String columnName) {
        return getCellValue(columnName, null);
    }

}
