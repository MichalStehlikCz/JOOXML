package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegion;

/**
 * Class represents region that reads from parameters or single row query.
 * Copies cells and populates them with values from data source as specified in region definition
 */
public class RowAreaBuilder extends RowRegionBuilder {

    private int minRow;
    private int maxRow;

    public int getMinRow() {
        return minRow;
    }

    public void setMinRow(int minRow) {
        this.minRow = minRow;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    @Override
    public int getFirstCoveredRow() {
        return getMinRow();
    }

    @Override
    public int getLastCoveredRow() {
        return getMaxRow();
    }

    @Override
    public void validate() {
        if (minRow < 0) {
            throw new IllegalArgumentException("Row cannot be negative");
        }
        if (minRow>maxRow) {
            throw new IllegalArgumentException("First row must be before last in row area (" + minRow + ", " + maxRow + ")");
        }
        super.validate();
    }

    @Override
    public ReportRegion build(TemplateWorkbook template) {
        return new RowArea(getNameNm(), getReportDataSource(), maxRow - minRow + 1, buildRows(template)
                , buildSubRegions(template));
    }

}
