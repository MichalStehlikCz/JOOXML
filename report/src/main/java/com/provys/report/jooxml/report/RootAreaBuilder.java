package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

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

    /**
     * Root area must start at the beginning of sheet
     */
    @Nonnull
    @Override
    RootAreaBuilder setFirstRow(Integer firstRow) {
        if (firstRow != 0) {
            throw new RuntimeException("Root area has to start on the first row");
        }
        return this;
    }

    /**
     * @return 0 as root area starts on the first row
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffFirstRow() {
        return Optional.of(0);
    }

    /**
     * @return effective last row of region; if last row is not specified and cannot be guessed from children, uses
     * max integer
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffLastRow() {
        return super.getEffLastRow().or(() -> Optional.of(Integer.MAX_VALUE));
    }

    RootAreaBuilder setDataSource(String dataSourceName) {
        if (dataSourceName.isEmpty()) {
            throw new IllegalArgumentException("Datasource name cannot be empty");
        }
        this.dataSourceName = dataSourceName;
        return this;
    }

    private void validateDataSource(Map<String, ReportDataSource> dataSources) {
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

    @Override
    public void validate(Map<String, ReportDataSource> dataSources) {
        super.validate(dataSources);
        validateDataSource(dataSources);
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        if (dataSource == null) {
            return super.doBuild(template);
        } else {
            return new DataReader(getNameNm().orElseThrow() + "DATA", dataSource,
                    new RowParentArea(getNameNm().orElseThrow() /*empty should be caught during validation */,
                            doBuildChildren(template)));
        }
    }
}
