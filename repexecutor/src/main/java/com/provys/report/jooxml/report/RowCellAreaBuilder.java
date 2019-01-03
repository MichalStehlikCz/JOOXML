package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellReference;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RowCellAreaBuilder extends RowAreaBuilder<RowCellAreaBuilder> {

    final private Map<CellAddress, FieldBind> fieldBinds;

    /**
     * Default constructor of empty area.
     */
    public RowCellAreaBuilder() {
        fieldBinds = new ConcurrentHashMap<>(5);
    }

    /**
     * @return list of value rules, applicable on area
     */
    private Map<CellAddress, FieldBind> getFieldBinds() {
        return Collections.unmodifiableMap(fieldBinds);
    }

    /**
     * Adds value rule to region. Verifies if value rule is vlid for given region before insertion.
     *
     * @param fieldBind is rule to be used for populating region
     */
    public RowCellAreaBuilder addFieldBind(FieldBind fieldBind) {
        fieldBinds.put(fieldBind.getCellAddress(), fieldBind);
        return self();
    }

    /**
     * Retrieves field bind for particular cell
     *
     * @param row is row index of cell being queried
     * @param col is column index of cell being queried
     * @return reference to field bind if one exists, empty optional if no bind exists for given field
     */
    public Optional<FieldBind> getFieldBindAt(int row, int col) {
// TODO fix this
//        return Optional.ofNullable(fieldBinds.get(new CellAddress(row, col)));
        throw new RuntimeException("RowCellAreaBuilder - not implemented");
    }

    @Override
    protected RowCellAreaBuilder self() {
        return this;
    }

    /**
     * Verifies that specified field bind is valid for this region.
     * Generally checks if referenced cell is within region's own area and is not in one of subregion's areas.
     *
     * @param fieldBind is field binding to be validated
     */
    private void validateFieldBind(FieldBind fieldBind) {
        Objects.requireNonNull(fieldBind);
        if (fieldBind.getCellAddress().getRow() < getFirstRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getFirstRow() + ", bind row " + fieldBind.getCellAddress().getRow());
        }
        if (fieldBind.getCellAddress().getRow() > getLastRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getLastRow() + ", bind row " + fieldBind.getCellAddress().getRow());
        }
    }

    /**
     * Runs validation on all field binds in region.
     */
    private void validateFieldBinds() {
        for (FieldBind fieldBind : fieldBinds.values()) {
            validateFieldBind(fieldBind);
        }
    }

    @Override
    public void validate() {
        super.validate();
        validateFieldBinds();
    }

    /**
     * @return collection of rows for this region
     */
    private Collection<Row> buildRows(TplWorkbook template) {
        // first we will go through cells in template and we will try to find corresponding bindings
        return Stream.concat(
                Stream.concat( // rows built from cells
                        template.getSheet()// cells retrieved from template
                                .getRows(getFirstRow(), getLastRow())
                                .stream()
                                .flatMap(row -> row.getCells().stream())
                                .filter(cell -> this.isInTemplateRegion(cell.getRowIndex(), cell.getColIndex()))
                                .map(cell -> new CellBuilder(cell, this))
                        , getFieldBinds().values().stream() // cells construed from required field binds
                                .map(fieldBind -> new CellBuilder(fieldBind, this))
                ).collect(Collectors.groupingBy(CellBuilder::getRowIndex
                        , new RowBuilder.RowBuilderCollector())).values().stream()
                , template.getSheet() // rows built from template (no cells, but row height etc. )
                        .getRows(getFirstRow(), getLastRow())
                        .stream()
                        .map(row -> new RowBuilder(row, this))
        ).collect(Collectors.groupingBy(RowBuilder::getRowIndex, new RowBuilder.AreaRowCollector())).values();
    }

    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        return new RowCellArea(getNameNm(), isTopLevel(), getLastRow() - getFirstRow() + 1, buildRows(template));
    }

}
