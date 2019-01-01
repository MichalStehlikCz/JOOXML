package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import org.apache.poi.ss.util.CellReference;

import java.util.*;

/**
 * Class is used to build ReportStep based on underlying template workbook and information about binds and subregions.
 * It provides all necessary validations and at the end, creates ReportStep of specific type, that is non-mutable
 * and supplies view of underlying data useful during report execution.
 *
 * All cell coordinates correspond to position in template workbook; they are recalculated to positions relative to
 * parent region when building ReportStep.
 *
 * Class is not thread-safe, it is expected to onl be used when building report definition within single thread
 */
abstract class RowAreaBuilder<T extends RowAreaBuilder> implements StepBuilder {

    private String nameNm;
    private boolean topLevel = false;
    private int firstRow = -1;
    private int lastRow = -1;
    private Optional<ReportDataSource> reportDataSource;

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
     * @return self to allow fluent build
     */
    public T setNameNm(String nameNm) {
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name cannot be empty string");
        }
        this.nameNm = nameNm;
        return self();
    }

    /**
     * @return first row in template covered by area; returns -1 if it has not been initialized yet
     */
    public int getFirstRow() {
        return firstRow;
    }

    /**
     * Set first row in template covered by region.
     *
     * @param firstRow is first row covered by given region
     * @return self to allow fluent build
     */
    public T setFirstRow(int firstRow) {
        if (firstRow < 0) {
            throw new IllegalArgumentException("First row cannot be negative");
        }
        this.firstRow = firstRow;
        return self();
    }

    /**
     * @return last row in template covered by area; returns -1 if it has not been initialized et
     */
    public int getLastRow() {
        return lastRow;
    }

    /**
     * Set last row in template covered by region.
     *
     * @param lastRow is last row in template coveerd by region
     * @return self to allow fluent build
     */
    public T setLastRow(int lastRow) {
        if (lastRow < 0) {
            throw new IllegalArgumentException("Last row cannot be negative");
        }
        this.lastRow = lastRow;
        return self();
    }

    /**
     * @return indication if region is top level region (e.g. no region above will initialize rows and region should
     * flush rows)
     */
    public boolean isTopLevel() {
        return topLevel;
    }

    /**
     * Sets indicationthat region is top level region
     *
     * @param topLevel flag indicating if reion should be considered top level or not
     */
    public T setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
        return self();
    }

    /**
     * @return value indicating if cell with given position is inside template area of region
     */
    public boolean isInTemplateRegion(int rowIndex, int columnIndex) {
        return (rowIndex >= getFirstRow()) && (rowIndex <= getLastRow());
    }

    /**
     * @return value indicating if cell with given position is inside template area of region
     */
    public boolean isInTemplateRegion(CellReference cell) {
        return isInTemplateRegion(cell.getRow(), cell.getCol());
    }

    /**
     * @return data source used to populate this region
     */
    public Optional<ReportDataSource> getReportDataSource() {
        return reportDataSource;
    }

    /**
     * Set specified data source as report region's data source
     *
     * @param reportDataSource is data source to be used for region
     * @return self to allow fluent build
     */
    public T setReportDataSource(ReportDataSource reportDataSource) {
        this.reportDataSource = Optional.of(reportDataSource);
        return self();
    }

    /**
     * Validates all properites of this region.
     * Base implementation validates all field binds and subregions. Subclasses might add additional rules on what is
     * actually considered valid region definition
     */
    protected void validate() {
        if (getNameNm() == null) {
            throw new IllegalStateException("Internal name not initialized");
        }
        if (getFirstRow() < 0) {
            throw new IllegalStateException("First row of covered area has not been initialized");
        }
        if (getLastRow() < 0) {
            throw new IllegalStateException("Last row of covered area has not been initialized");
        }
        if (getFirstRow() > getLastRow()) {
            throw new IllegalStateException("First row of region has to be above last row");
        }
    }

    /**
     * Builds region from this builder. Called from build after validation.
     */
    abstract protected ReportStep doBuild(TemplateWorkbook template);

    /**
     * Method allows fluent build for children of this type
     *
     * @return properly typed self
     */
    abstract protected T self();

    /**
     * Validates and builds step from builder.
     */
    public ReportStep build(TemplateWorkbook template) {
        validate();
        return doBuild(template);
    }
}
