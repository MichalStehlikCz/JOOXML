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

    // data source is filled in internally during validation
    @Nullable
    private ReportDataSource dataSource = null;

    RootAreaBuilder() {
        super(null);
    }

    /**
     * @return Root area defines its own datasource - root datasource
     */
    @Override
    @Nonnull
    public ReportDataSource getDataSource() {
        if (dataSource == null) {
            throw new RuntimeException("Root region cannot be queried for datasource until validation");
        }
        return dataSource;
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

    @Override
    protected void validateParent() {
        if (getParent().isPresent()) {
            throw new RuntimeException("Root step should not have parent");
        }
    }

    private void validateDataSource(Map<String, ReportDataSource> dataSources) {
        dataSource = dataSources.get("ROOT");
        if (dataSource == null) {
            throw new RuntimeException("DataSource not found in report using name ROOT");
        }
        if (dataSource.getParent().isPresent()) {
            throw new RuntimeException("ROOT data source should not have parent");
        }
    }

    @Override
    protected void doValidate(Map<String, ReportDataSource> dataSources) {
        super.doValidate(dataSources);
        validateDataSource(dataSources);
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        if (dataSource == null) {
            throw new RuntimeException("Root data source should have been set during validation");
        }
        return new DataReader(getNameNm().orElseThrow() + "DATA", dataSource,
                new ParentStep(getNameNm().orElseThrow() /*empty should be caught during validation */,
                        doBuildChildren(template)));
    }
}
