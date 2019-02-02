package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RowCellAreaBuilder extends RowRegionBuilder<RowCellAreaBuilder> {

    @Nonnull
    private final Map<CellCoordinates, CellBind> fieldBinds;

    /**
     * Default constructor of empty area.
     */
    RowCellAreaBuilder(@Nullable StepBuilder parent) {
        super(parent);
        fieldBinds = new ConcurrentHashMap<>(5);
    }

    @Nonnull
    @Override
    public String getDefaultNameNmPrefix() {
        return "CELLAREA";
    }

    @Nonnull
    @Override
    public String proposeChildName(StepBuilder child) {
        return child.getDefaultNameNmPrefix();
    }

    /**
     * @return list of value rules, applicable on area
     */
    private Map<CellCoordinates, CellBind> getFieldBinds() {
        return Collections.unmodifiableMap(fieldBinds);
    }

    /**
     * Adds value rule to region. Verifies if value rule is vlid for given region before insertion.
     *
     * @param cellBind is rule to be used for populating region
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nonnull
    RowCellAreaBuilder addFieldBind(CellBind cellBind) {
        fieldBinds.put(cellBind.getCoordinates(), cellBind);
        return self();
    }

    @Nonnull
    @Override
    protected RowCellAreaBuilder self() {
        return this;
    }

    /**
     * Verifies that specified field bind is valid for this region.
     * Generally checks if referenced cell is within region's own area and is not in one of subregion's areas.
     *
     * @param cellBind is field binding to be validated
     */
    private void validateFieldBind(CellBind cellBind) {
        Objects.requireNonNull(cellBind);
        if (cellBind.getCoordinates().getRow() < getFirstRow().orElseThrow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getFirstRow() + ", bind row " + cellBind.getCoordinates().getRow());
        }
        if (cellBind.getCoordinates().getRow() > getLastRow().orElseThrow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getLastRow() + ", bind row " + cellBind.getCoordinates().getRow());
        }
    }

    /**
     * Runs validation on all field binds in region.
     */
    private void validateFieldBinds() {
        for (CellBind cellBind : fieldBinds.values()) {
            validateFieldBind(cellBind);
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
    @Nonnull
    private Collection<Row> buildRows(TplWorkbook template) {
        // first we will go through cells in template and we will try to find corresponding bindings
        return Stream.concat(
                Stream.concat( // rows built from cells
                        template.getSheet()// cells retrieved from template
                                .getRows(getFirstRow().orElseThrow(), getLastRow().orElseThrow()) // validate ensures
                                                                                   // there will be no exception here
                                .stream()
                                .flatMap(row -> row.getCells().stream())
                                .map(cell -> new CellBuilder(cell, this))
                        , getFieldBinds().values().stream() // cells construed from required field binds
                                .map(cellBind -> new CellBuilder(cellBind, this))
                ).collect(Collectors.groupingBy(CellBuilder::getRowIndex
                        , new RowBuilder.RowBuilderCollector())).values().stream()
                , template.getSheet() // rows built from template (no cells, but row height etc. )
                        .getRows(getFirstRow().orElseThrow(), getLastRow().orElseThrow()) // validate ensures there will
                                                                                          // be no exception here
                        .stream()
                        .map(row -> new RowBuilder(row, this))
        ).collect(Collectors.groupingBy(RowBuilder::getRowIndex, new RowBuilder.AreaRowCollector())).values();
    }

    @Nonnull
    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        return new RowCellArea(getNameNm().orElseThrow() /* empty should be handled by validation */, isTopLevel(),
                getLastRow().orElseThrow() - getFirstRow().orElseThrow() + 1, buildRows(template));
    }
}
