package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegion;

public class RowTableBuilder extends RowRegionBuilder {

    private int firstCoveredRow;
    private int lastCoveredRow;
    private int firstTemplateRow;
    private int lastTemplateRow;

    @Override
    public int getFirstCoveredRow() {
        return firstCoveredRow;
    }

    public void setFirstCoveredRow(int firstCoveredRow) {
        this.firstCoveredRow = firstCoveredRow;
    }

    @Override
    public int getLastCoveredRow() {
        return lastCoveredRow;
    }

    public void setLastCoveredRow(int lastCoveredRow) {
        this.lastCoveredRow = lastCoveredRow;
    }

    @Override
    public int getFirstTemplateRow() {
        return firstTemplateRow;
    }

    public void setFirstTemplateRow(int firstTemplateRow) {
        this.firstTemplateRow = firstTemplateRow;
    }

    @Override
    public int getLastTemplateRow() {
        return lastTemplateRow;
    }

    public void setLastTemplateRow(int lastTemplateRow) {
        this.lastTemplateRow = lastTemplateRow;
    }

    public int getHeight() {
        return getLastTemplateRow() - getFirstTemplateRow() + 1;
    }

    @Override
    public void validate() {
        if (getFirstCoveredRow() < 0) {
            throw new IllegalArgumentException("Row cannot be negative");
        }
        if (getFirstTemplateRow() < getFirstCoveredRow()) {
            throw new IllegalArgumentException("Template area cannot start above covered area");
        }
        if (getLastTemplateRow() < getFirstTemplateRow()) {
            throw new IllegalArgumentException("Template area cannot start before its end");
        }
        if (getLastCoveredRow() < getLastTemplateRow()) {
            throw new IllegalArgumentException("Tenplate area cannot go after covered area");
        }
        super.validate();
    }

    @Override
    public ReportRegion build(TemplateWorkbook template) {
        return new RowArea(getNameNm(), getReportDataSource(), getHeight(), buildRows(template)
                , buildSubRegions(template));
    }

}
