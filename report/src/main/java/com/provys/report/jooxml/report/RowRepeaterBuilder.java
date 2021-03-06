package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellReference;

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
    Optional<String> getDataSourceName() {
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

    @Override
    public void validateCellReferences(TplWorkbook template) {
        if (child != null) {
            child.validateCellReferences(template);
        }
    }

    private void validatePathReady() {
        if (firstBodyRow == null) {
            throw new RuntimeException("Cannot evaluate cell path - first body row not known");
        }
        if (lastBodyRow == null) {
            throw new RuntimeException("Cannot evaluate cell path - last body row not known");
        }
        if (child == null) {
            throw new RuntimeException("Cannot evaluate cell path - child not known");
        }
    }

    private int calcRelativeRecordNr(CellReference cellReference) {
        var row = cellReference.getRow();
        //noinspection ConstantConditions - verified by prior validatePathReady call
        if (row < firstBodyRow) {
            //noinspection ConstantConditions - verified by prior validatePathReady call
            return (row - lastBodyRow) / (lastBodyRow - firstBodyRow + 1);
        } else //noinspection ConstantConditions - verified by prior validatePathReady call
            if (row > lastBodyRow) {
            return (row - firstBodyRow) / (lastBodyRow - firstBodyRow + 1);
        } else {
            return 0;
        }
    }

    private void validateRelativePath(StepBuilder fromArea, CellReference cellReference) {
        // from area is descendant of this builder -> we build relative reference
        int recordNr = calcRelativeRecordNr(cellReference);
        //noinspection ConstantConditions - verified by prior call to validatePathReady
        var newCellReference = cellReference.shiftBy(- recordNr * (lastBodyRow - firstBodyRow + 1), 0);
        //noinspection ConstantConditions - verified by prior call to validatePathReady
        child.validatePath(fromArea, newCellReference);
    }

    private static class RecordNrAndCellReference {
        private int recordNr;
        private CellReference cellReference;
    }

    @Nonnull
    private RecordNrAndCellReference calcAbsoluteRecordNr(CellReference cellReference) {
        var row = cellReference.getRow();
        RecordNrAndCellReference result = new RecordNrAndCellReference();
        //noinspection ConstantConditions - verified by prior call to validatePathReady
        if (row < firstBodyRow) {
            // absolute reference from start
            var effFirstRow = getEffFirstRow().orElseThrow(
                    () -> new RuntimeException("Cannot evaluate relative cell path - effective first row now known"));
            //noinspection ConstantConditions - verified by prior call to validatePathReady
            result.recordNr = (row - effFirstRow) / (lastBodyRow - firstBodyRow + 1);
            result.cellReference = cellReference.shiftBy(
                    firstBodyRow - effFirstRow - result.recordNr * (lastBodyRow - firstBodyRow + 1), 0);
        } else //noinspection ConstantConditions - verified by prior call to validatePathReady
            if (row > lastBodyRow) {
                // absolute reference from end
                var effLastRow = getEffLastRow().orElseThrow(
                        () -> new RuntimeException("Cannot evaluate relative cell path - effective last row now known"));
                result.recordNr = (row - effLastRow) / (lastBodyRow - firstBodyRow + 1) - 1;
                result.cellReference = cellReference.shiftBy(
                        lastBodyRow - effLastRow - (result.recordNr + 1) * (lastBodyRow - firstBodyRow + 1), 0);
            } else {
                throw new RuntimeException("Cannot set cell reference from outside of repeater to body (cell "
                        + cellReference + '"');
            }
            return result;
    }

    private void validateAbsolutePath(StepBuilder fromArea, CellReference cellReference) {
        var recordNrAndCellReference = calcAbsoluteRecordNr(cellReference);
        //noinspection ConstantConditions - verified by prior call to validatePathReady
        child.validatePath(fromArea, recordNrAndCellReference.cellReference);
    }

    @Override
    public void validatePath(StepBuilder fromArea, CellReference cellReference) {
        validatePathReady();
        if (fromArea.isAncestor(this)) {
            validateRelativePath(fromArea, cellReference);
        } else {
            validateAbsolutePath(fromArea, cellReference);
        }
    }

    @Nonnull
    @Override
    public Optional<AreaCellPath> getPath(StepBuilder fromArea, CellReference cellReference) {
        validatePathReady();
        if (fromArea.isAncestor(this)) {
            // from area is descendant of this builder -> we build relative reference
            var recordNr = calcRelativeRecordNr(cellReference);
            //noinspection ConstantConditions - verified by prior call to validatePathReady
            var newCellReference = cellReference.shiftBy(
                    - recordNr * (lastBodyRow - firstBodyRow + 1), 0);
            //noinspection ConstantConditions - verified by prior call to validatePathReady
            return child.getPath(fromArea, newCellReference).
                    map(childPath -> new AreaCellPathRelativeRecord(childPath, recordNr));
        } else {
            var recordNrAndCellReference = calcAbsoluteRecordNr(cellReference);
            //noinspection ConstantConditions - verified by prior call to validatePathReady
            return child.getPath(fromArea, recordNrAndCellReference.cellReference).
                    map(childPath -> new AreaCellPathAbsoluteRecord(childPath, recordNrAndCellReference.recordNr));
        }
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
    private void validateChild(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
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
        child.validate(dataSources, template);
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
    protected void afterValidate(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        validateChild(dataSources, template);
        validateBody();
        super.afterValidate(dataSources, template);
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        return new DataRepeater(getNameNm().orElseThrow(), getDataSource(),
                getChild().orElseThrow().doBuild(template)); // orElseThrow should be pre-verified
    }

}
