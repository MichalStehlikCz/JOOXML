package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionCell;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import org.apache.poi.ss.util.CellReference;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class is used to build ReportRegion based on underlying template workbook and information about binds and subregions.
 * It provides all necessary validations and at the end, creates ReportRegion of specific type, that is non-mutable
 * and supplies view of underlying data useful during report execution.
 *
 * All cell coordinates correspond to position in template workbook; they are recalculated to positions relative to
 * parent region when building ReportRegion.
 *
 * Class is not thread-safe, it is expected to onl be used when building report definition within single thread
 */
abstract class RowRegionBuilder {

    final private Map<CellReference, FieldBind> fieldBinds;
    final private SortedSet<RowRegionBuilder> subRegions;
    private ReportDataSource reportDataSource;

    /**
     * Default constructor of empty area.
     */
    public RowRegionBuilder() {
        fieldBinds = new ConcurrentHashMap<>(5);
        subRegions = new TreeSet<>(Comparator.comparingInt(RowRegionBuilder::getFirstCoveredRow));
    }

    /**
     * @return list of value rules, applicable on area
     */
    public Map<CellReference, FieldBind> getFieldBinds() {
        return Collections.unmodifiableMap(fieldBinds);
    }

    /**
     * Adds value rule to region. Verifies if value rule is vlid for given region before insertion.
     *
     * @param fieldBind is rule to be used for populating region
     */
    public void addFieldBind(FieldBind fieldBind) {
        fieldBinds.put(fieldBind.getCellReference(), fieldBind);
    }

    /**
     * Retrieves field bind for particular cell
     *
     * @param row is row index of cell being queried
     * @param col is column index of cell being queried
     * @return reference to field bind if one exists, empty optional if no bind exists for given field
     */
    public Optional<FieldBind> getFieldBindAt(int row, int col) {
        return Optional.ofNullable(fieldBinds.get(new CellReference(row, col)));
    }

    /**
     * @return first row in template covered by area
     */
    abstract int getFirstCoveredRow();

    /**
     * @return last row in template covered by area
     */
    abstract int getLastCoveredRow();

    /**
     * Defines start of area that is actually used as template for region.
     * By default, it is the same as covered area, but for repeaters, template area is single row while there might
     * be multiple rows present in template spreadsheet
     *
     * @return first row in template sheet acting as template
     */
    public int getFirstTemplateRow() {
        return getFirstCoveredRow();
    }

    /**
     * Defines end of area that is actually used as template for region.
     * By default, it is the same as covered area, but for repeaters, template area is single row while there might
     * be multiple rows present in template spreadsheet
     *
     * @return last row in template sheet acting as template
     */
    public int getLastTemplateRow() {
        return getLastCoveredRow();
    }

    /**
     * @return value indicating if cell with given position is inside template area of region
     */
    public boolean isInTemplateRegion(int rowIndex, int columnIndex) {
        return (rowIndex >= getFirstTemplateRow()) && (rowIndex <= getLastTemplateRow());
    }

    /**
     * @return value indicating if cell with given position is inside template area of region
     */
    public boolean isInTemplateRegion(CellReference cell) {
        return isInTemplateRegion(cell.getRow(), cell.getCol());
    }

    /**
     * Iterator for iteration through subregions
     */
    public Iterator<RowRegionBuilder> subRegions() {
        return subRegions.iterator();
    }

    /**
     * Ietrator over subregions, for Iterable implementation
     */
    public Iterator<RowRegionBuilder> iterator() {
        return subRegions();
    }

    /**
     * @return subregions as list
     */
    public Collection<RowRegionBuilder> getSubRegions() {
        return Collections.unmodifiableCollection(subRegions);
    }

    /**
     * Add subregion to list of subregions
     */
    public void addSubRegion(RowRegionBuilder subRegion) {
        if (!subRegions.add(subRegion)) {
            throw new IllegalArgumentException("Subregion with same covered first row already exists");
        }
    }
    /**
     * @return data source used to populate this region
     */
    public ReportDataSource getReportDataSource() {
        return reportDataSource;
    }

    /**
     * Set specified data source as report region's data source
     */
    public void setReportDataSource(ReportDataSource reportDataSource) {
        this.reportDataSource = Objects.requireNonNull(reportDataSource);
    }

    /**
     * Verifies that specified field bind is valid for this region.
     * Generally checks if referenced cell is within region's own area and is not in one of subregion's areas.
     *
     * @param fieldBind is field binding to be validated
     */
    protected void validateFieldBind(FieldBind fieldBind) {
        Objects.requireNonNull(fieldBind);
        if (fieldBind.getCellReference().getRow() < getFirstTemplateRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getFirstCoveredRow() + ", bind row " + fieldBind.getCellReference().getRow());
        }
        if (fieldBind.getCellReference().getRow() > getLastTemplateRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getFirstCoveredRow() + ", bind row " + fieldBind.getCellReference().getRow());
        }
        for (RowRegionBuilder subRegion : subRegions) {
            if ((fieldBind.getCellReference().getRow() >= subRegion.getFirstCoveredRow())
                    && (fieldBind.getCellReference().getRow() <= subRegion.getLastCoveredRow())) {
                throw new IllegalArgumentException("Data bind inside subregion " + fieldBind.toString());
            }
        }
    }

    /**
     * Runs validation on all field binds in region.
     */
    protected void validateFieldBinds() {
        for (FieldBind fieldBind : fieldBinds.values()) {
            validateFieldBind(fieldBind);
        }
    }

    /**
     * Validates sub-region.
     * Checks that sub-region falls into this region's area. Function does not validate overlaps with other subregions
     * and field binds as these are expected to be validated outside elsewhere
     *
     * @param subRegion is sub-region being validated
     */
    protected void validateSubRegion(RowRegionBuilder subRegion) {
        Objects.requireNonNull(subRegion);
        // subregion must be inside area covered by region
        if (subRegion.getFirstCoveredRow() < getFirstCoveredRow()) {
            throw new IllegalArgumentException("First row of sub-region must be inside region area");
        }
        if (subRegion.getLastCoveredRow() > getLastCoveredRow()) {
            throw new IllegalArgumentException("Last row of region validity must be inside region area");
        }
    }

    /**
     * Validate sub-regions. Calls validate on each region plus checks that regions are in ascending order of first row
     * and do not overlap.
     */
    protected void validateSubRegions() {
        RowRegionBuilder previousRegion = null;
        for (RowRegionBuilder subRegion : subRegions) {
            validateSubRegion(subRegion);
            if (previousRegion != null) {
                if (previousRegion.getLastCoveredRow() >= subRegion.getFirstCoveredRow()) {
                    throw new IllegalArgumentException("Regions overlap");
                }
            }
        }
    }

    /**
     * Validates all properites of this region.
     * Base implementation validates all field binds and subregions. Subclasses might add additional rules on what is
     * actually considered valid region definition
     */
    public void validate() {
        validateFieldBinds();
        validateSubRegions();
    }

    /**
     * Class used to track and merge coordinates in combined cell
     */
    private static class Coordinates {
        int row;
        int column;

        Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other instanceof Coordinates) {
                return ((row == ((Coordinates) other).row) && (column == ((Coordinates) other).column));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return column + 31*row;
        }
    }

    /**
     * Class represents tuple of row index and report region cell... but as java doesn't have tuples
     */
    private static class ReportRegionCellWithRow {
        private final int rowIndex;
        private final ReportRegionCell cell;

        ReportRegionCellWithRow(int rowIndex, ReportRegionCell cell) {
            this.rowIndex = rowIndex;
            this.cell = Objects.requireNonNull(cell);
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public ReportRegionCell getCell() {
            return cell;
        }
    }
    /**
     * Class used to merge template cells and field bind
     */
    private static class ReportRegionCellBuilder {

        private Coordinates index;
        private TemplateCell templateCell;
        private String bindColumn;

        ReportRegionCellBuilder() {
            this.index = null;
            this.templateCell = null;
            this.bindColumn = null;
        }

        ReportRegionCellBuilder(TemplateCell templateCell) {
            this.index = new Coordinates(templateCell.getRowIndex(), templateCell.getColumnIndex());
            this.templateCell = templateCell;
            this.bindColumn = null;
        }

        ReportRegionCellBuilder(FieldBind fieldBind) {
            this.index = new Coordinates(fieldBind.getCellReference().getRow(), fieldBind.getCellReference().getCol());
            this.templateCell = null;
            this.bindColumn = fieldBind.getSourceColumn();
        }

        Coordinates getIndex() {
            return index;
        }

        ReportRegionCellBuilder combine(ReportRegionCellBuilder cell) {
            if (this.index == null) {
                this.index = cell.index;
            } else if ((cell.index != null) && (!cell.index.equals(cell.index))) {
                throw new IllegalArgumentException("Cannot merge two combined cells with different coordinates");
            }
            if (this.templateCell == null) {
                this.templateCell = cell.templateCell;
            } else if (cell.templateCell != null) {
                throw new IllegalArgumentException("Cannot merge two template cells");
            }
            if (this.bindColumn == null) {
                this.bindColumn = cell.bindColumn;
            } else if (cell.bindColumn != null) {
                throw new IllegalArgumentException("Cannot merge two bind cells");
            }
            return this;
        }

        ReportRegionCellWithRow build() {
            if (index == null) {
                throw new IllegalStateException("Cannot build region cell from empty combined cell");
            }
            ReportRegionCell cell;
            if (templateCell != null) {
                cell = new TemplateCellWithBind(index.column, templateCell, Optional.ofNullable(bindColumn));
            } else {
                if (bindColumn == null) {
                    throw new IllegalStateException("Cannot build region cell from empty combined cell");
                }
                cell = new EmptyCellWithBind(index.column, bindColumn);
            }
            return new ReportRegionCellWithRow(index.row, cell);
        }
    }

    /**
     * Collector used to combine partial cells into ReportRegionCells
     */
    private static class CellBuilderCollector implements Collector<ReportRegionCellBuilder, ReportRegionCellBuilder
            , ReportRegionCellWithRow> {

        @Override
        public Supplier<ReportRegionCellBuilder> supplier() {
            return ReportRegionCellBuilder::new;
        }

        @Override
        public BiConsumer<ReportRegionCellBuilder, ReportRegionCellBuilder> accumulator() {
            return ReportRegionCellBuilder::combine;
        }

        @Override
        public BinaryOperator<ReportRegionCellBuilder> combiner() {
            return ReportRegionCellBuilder::combine;
        }

        @Override
        public Function<ReportRegionCellBuilder, ReportRegionCellWithRow> finisher() {
            return ReportRegionCellBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return null;
        }
    }

    private static class ReportRegionRowBuilder {
        int rowIndex;

    }
    /**
     * Collector used to combine cells into report region rows
     */
    private static class RowBuilderCollector implements Collector<ReportRegionCellWithRow, ReportRegionRowBuilder, ReportRegionRow> {

    }
    /**
     * Class collects cells in row; cells are held in map, indexed by cell index unlike ReportRegionRow that holds cells
     * in flat collection
     */
    private static class Row {
        private final int rowNumber;
        private final Map<Integer, ReportRegionCell> cells;

        Row(Integer rowNumber, Map<Integer, ReportRegionCell> cells) {
            this.rowNumber = rowNumber;
            this.cells = cells;
        }

        public int getRowNumber() {
            return rowNumber;
        }

        public Map<Integer, ReportRegionCell> getCells() {
            return cells;
        }
    }
    /**
     * @return collection of rows for this region
     */
    protected Collection<ReportRegionRow> buildRows(TemplateWorkbook template) {
        // first we will go through cells in template and we will try to find corresponding bindings
        return Stream.concat(
                template.getSheet()
                        .getRows(getFirstTemplateRow(), getLastTemplateRow())
                        .stream()
                .flatMap(row -> row.getCells().stream())
                .filter(cell -> this.isInTemplateRegion(cell.getRowIndex(), cell.getColumnIndex()))
                .map(cell -> new ReportRegionCellBuilder(cell))
                , getFieldBinds().values().stream()
                .map(fieldBind -> new ReportRegionCellBuilder(fieldBind))
        ).collect(Collectors.groupingBy(ReportRegionCellBuilder::getIndex, new CellBuilderCollector()))
        .values().stream()
        .collect(Collectors.groupingBy(ReportRegionCellWithRow::getRowIndex, new RowBuilderCollector()));
    }

    /**
     * Builds region from this builder
     */
    abstract public ReportRegion build(TemplateWorkbook template);
}
