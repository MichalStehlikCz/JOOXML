package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        @Nullable Class<?> prefClass;
        if (prefType == null) {
            prefClass = null;
        } else {
            switch (prefType) {
                case FORMULA:
                    LOG.warn("GetRecordValue: Request to retrieve data value made for cell type formula");
                    prefClass = null;
                    break;
                case STRING:
                    prefClass = String.class;
                    break;
                case NUMERIC:
                    prefClass = Double.class;
                    break;
                case BOOLEAN:
                    prefClass = Boolean.class;
                    break;
                case ERROR:
                    LOG.warn("GetRecordValue: Request to retrieve data value made for cell type error");
                    prefClass = null;
                    break;
                default:
                    prefClass = null;
            }
        }
        Optional<Object> optValue = getValue(columnName, prefClass);
        final CellValueFactory cellValueFactory = reportContext.getCellValueFactory();
        CellValue result;
        if (optValue.isEmpty()) {
            if (prefType == null) {
                result = cellValueFactory.getBlank();
            } else {
                switch (prefType) {
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
            switch (value.getClass().getName()) {
                case "String":
                    result = cellValueFactory.ofString((String) value);
                    break;
                case "Double":
                    result = cellValueFactory.ofNumeric((Double) value);
                    break;
                case "Boolean":
                    result = cellValueFactory.ofBoolean((Boolean) value);
                    break;
                default:
                    result = cellValueFactory.ofString(value.toString());
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
