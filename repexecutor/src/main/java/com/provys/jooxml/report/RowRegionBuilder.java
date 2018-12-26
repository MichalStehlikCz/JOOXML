package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.repexecutor.ReportRegionRow;
import org.apache.poi.ss.util.CellReference;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private String nameNm;
    private int offset; // set during subregion validation.
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
     * @return internal name of row area; might be null if it was not initialized yet
     */
    public String getNameNm() {
        return nameNm;
    }

    /**
     * Set internal name of report region
     *
     * @param nameNm is new internal name
     */
    public void setNameNm(String nameNm) {
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name cannot be empty string");
        }
        this.nameNm = nameNm;
    }

    /**
     * @return offset from parent region (0 means starts on the same row as parent region) / previous subregion (0 means
     * starts right after last line of previous subregion)
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * Set offset of subregion. Usually called from subregion validation
     */
    protected void setOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }
        this.offset = offset;
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
        if (subRegion.getFirstCoveredRow() < getFirstTemplateRow()) {
            throw new IllegalArgumentException("First row of sub-region must be inside region area");
        }
        if (subRegion.getLastCoveredRow() > getLastTemplateRow()) {
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
            if (previousRegion == null) {
                subRegion.setOffset(subRegion.getFirstCoveredRow() - getFirstTemplateRow());
            } else {
                if (previousRegion.getLastCoveredRow() >= subRegion.getFirstCoveredRow()) {
                    throw new IllegalArgumentException("Regions overlap");
                }
                subRegion.setOffset(subRegion.getFirstCoveredRow() - previousRegion.getLastCoveredRow()-1);
            }
        }
    }

    /**
     * Validates all properites of this region.
     * Base implementation validates all field binds and subregions. Subclasses might add additional rules on what is
     * actually considered valid region definition
     */
    public void validate() {
        if (nameNm == null) {
            throw new IllegalStateException("Internal name not initialized");
        }
        validateFieldBinds();
        validateSubRegions();
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
                .map(cell -> new CellBuilder(cell, this))
                , getFieldBinds().values().stream()
                .map(fieldBind -> new CellBuilder(fieldBind, this))
        ).collect(Collectors.groupingBy(CellBuilder::getRowIndex
                , new RowBuilder.RowBuilderCollector())).values();
    }

    /**
     * @return collection of subregions for this region
     */
    protected Collection<ReportRegion> buildSubRegions(TemplateWorkbook template) {
        return subRegions.stream().map(subRegion -> subRegion.build(template)).collect(Collectors.toList());
    }

    /**
     * Builds region from this builder
     */
    abstract public ReportRegion build(TemplateWorkbook template);

}
