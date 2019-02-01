package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
class RootAreaBuilder extends RowParentAreaBuilder {

    // setters are used to fill data source name if required
    @Nullable
    private String dataSourceName = null;
    // data source is filled in internally during validation
    @Nullable
    private ReportDataSource dataSource = null;

    RootAreaBuilder() {
        super(null);
    }

    RootAreaBuilder setDataSource(String dataSourceName) {
        if (dataSourceName.isEmpty()) {
            throw new IllegalArgumentException("Datasource name cannot be empty");
        }
        this.dataSourceName = dataSourceName;
        return this;
    }

    @Override
    protected void validateDataSource(Map<String, ReportDataSource> dataSources) {
        if (dataSourceName != null) {
            dataSource = dataSources.get(dataSourceName);
            if (dataSource == null) {
                throw new RuntimeException("DataSource not found in report using name " + dataSourceName);
            }
            if (!dataSource.getParent().equals(getDataSource())) {
                throw new RuntimeException("DataSource parent does not match master step datasource");
            }
        }
    };

    @Nonnull
    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        if (dataSource == null) {
            return super.doBuild(template);
        } else {
            return new DataReader(getNameNm().orElseThrow() + "DATA", dataSource,
                    new RowParentArea(getNameNm().orElseThrow() /*empty should be caught during validation */,
                            doBuildChildren(template)));
        }
    }
}
