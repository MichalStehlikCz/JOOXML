package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
class RowRepeaterBuilder extends RowRegionBuilder<RowRepeaterBuilder> {

    // setters are used to fill data source name
    @Nullable
    private String dataSourceName = null;
    // data source is filled in internally during validation
    @Nullable
    private ReportDataSource dataSource = null;

    @Nullable
    private Integer firstBodyRow;
    @Nullable
    private Integer lastBodyRow;
    @Nullable
    private RowStepBuilder child;

    RowRepeaterBuilder(@Nullable StepBuilder parent) {
        super(parent);
    }

    @Nonnull
    @Override
    public String getDefaultNameNmPrefix() {
        return "REPEATER";
    }

    @Nonnull
    @Override
    public String proposeChildName(StepBuilder child) {
        return child.getDefaultNameNmPrefix();
    }

    @Nonnull
    @Override
    protected RowRepeaterBuilder self() {
        return this;
    }

    /**
     * @return value of field dataSourceName
     */
    @Nonnull
    public Optional<String> getDataSourceName() {
        return Optional.ofNullable(dataSourceName);
    }

    /**
     * Set value of field dataSourceName
     *
     * @param dataSourceName is new value to be set
     * @return self to enable chaining
     */
    RowRepeaterBuilder setDataSourceName(String dataSourceName) {
        if (dataSourceName.isEmpty()) {
            throw new RuntimeException("DataSource name cannot be empty");
        }
        this.dataSourceName = dataSourceName;
        return this;
    }

    /**
     * Repeater always has data source, thus it doesn't make sense to check parent. If its own datasource is not
     * evaluated yet, no point returning parent datasource that will be changed later anyways
     *
     * @return value of field dataSource
     */
    @Nonnull
    @Override
    public ReportDataSource getDataSource() {
        if (dataSource == null) {
            throw new RuntimeException("Root region cannot be queried for datasource until validation");
        }
        return dataSource;
    }

    /**
     * @return value of field firstBodyRow
     */
    @Nonnull
    Optional<Integer> getFirstBodyRow() {
        return Optional.ofNullable(firstBodyRow);
    }

    /**
     * Set value of field firstBodyRow
     *
     * @param firstBodyRow is new value to be set
     * @return self to enable chaining
     */
    @Nonnull
    RowRepeaterBuilder setFirstBodyRow(Integer firstBodyRow) {
        if (firstBodyRow < 0) {
            throw new RuntimeException("First row of body cannot be negative");
        }
        this.firstBodyRow = firstBodyRow;
        return this;
    }

    /**
     * @return value of field lastBodyRow
     */
    @Nonnull
    Optional<Integer> getLastBodyRow() {
        return Optional.ofNullable(lastBodyRow);
    }

    /**
     * Set value of field lastBodyRow
     *
     * @param lastBodyRow is new value to be set
     * @return self to enable chaining
     */
    RowRepeaterBuilder setLastBodyRow(Integer lastBodyRow) {
        if (lastBodyRow < 0) {
            throw new RuntimeException("Last row of body cannot be negative");
        }
        this.lastBodyRow = lastBodyRow;
        return this;
    }

    /**
     * @return value of field child
     */
    @Nonnull
    Optional<RowStepBuilder> getChild() {
        return Optional.ofNullable(child);
    }

    /**
     * Set value of field child
     *
     * @param child is new value to be set
     * @return self to enable chaining
     */
    @Nonnull
    RowRepeaterBuilder setChild(RowStepBuilder child) {
        this.child = Objects.requireNonNull(child);
        return this;
    }

    private void validateDataSource(Map<String, ReportDataSource> dataSources) {
        if (dataSourceName == null) {
            throw new RuntimeException("Datasource must be specified for RowRepeater step");
        }
        dataSource = dataSources.get(dataSourceName);
        if (dataSource == null) {
            throw new RuntimeException("DataSource not found in report using name " + dataSourceName);
        }
        if (!dataSource.getParent().equals(getParent().map(StepBuilder::getDataSource))) {
            throw new RuntimeException("DataSource parent does not match master step datasource");
        }
    }

    @SuppressWarnings({"squid:S3655", "OptionalGetWithoutIsPresent"}) // analysers do not underestand isEmpty is sufficient
    private void validateChild() {
        if (child == null) {
            throw new RuntimeException("Child must be specified for RowRepeater step");
        }
        if (child.getEffFirstRow().isEmpty()) {
            child.setEffFirstRow(getFirstBodyRow().
                    orElseThrow(() -> new RuntimeException("First body row must be specified in RowRepeater")));
        } else {
            int childEffFirstRow = child.getEffFirstRow().get();
            if (getFirstBodyRow().isEmpty()) {
                setFirstBodyRow(childEffFirstRow);
            } else if (getFirstBodyRow().get() != childEffFirstRow) {
                    throw new RuntimeException("Mismatch between first body row and first child row in RowRepeater");
            }
        }
        if (child.getEffLastRow().isEmpty()) {
            child.setEffLastRow(getLastBodyRow().
                    orElseThrow(() -> new RuntimeException("Last body row must be specified in RowRepeater")));
        } else {
            int childEffLastRow = child.getEffLastRow().get();
            if (getLastBodyRow().isEmpty()) {
                setLastBodyRow(childEffLastRow);
            } else if (getLastBodyRow().get() != childEffLastRow) {
                throw new RuntimeException("Mismatch between last body row and last child row in RowRepeater");
            }
        }
    }

    private void validateBody() {
        if (firstBodyRow == null) {
            throw new RuntimeException("First body row must be specified in RowRepeater");
        }
        if (lastBodyRow == null) {
            throw new RuntimeException("First body row must be specified in RowRepeater");
        }
        if (firstBodyRow < getEffFirstRow().orElseThrow()) { // should not throw - verified before...
            throw new RuntimeException("First row of body cannot be above first row in RowRepeater");
        }
        if (firstBodyRow > lastBodyRow) {
            throw new RuntimeException("Last row of body cannot be before the first");
        }
        if (lastBodyRow > getEffLastRow().orElseThrow()) { // should not throw - verified before...
            throw new RuntimeException("Last row of body cannot be after last row in RowRepeater");
        }
    }

    @Override
    protected void doValidate(Map<String, ReportDataSource> dataSources) {
        super.doValidate(dataSources);
        validateDataSource(dataSources);
    }

    @Override
    protected void afterValidate(Map<String, ReportDataSource> dataSources) {
        validateChild();
        validateBody();
        super.afterValidate(dataSources);
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        return new DataRepeater(getNameNm().orElseThrow(), getDataSource(),
                getChild().orElseThrow().doBuild(template)); // orElseThrow should be pre-verified
    }

}
