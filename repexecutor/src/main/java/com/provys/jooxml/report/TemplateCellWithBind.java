package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegionCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell from template workbook. Has coordinates relative to its parent region and holds reference to
 * bind column if one exists for the cell.
 */
class TemplateCellWithBind implements ReportRegionCell {

    private final int columnIndex; // index of column within row, zero based; in theory, index could be taken from
                                   // template cell as long as we do not have column regions, but...
    private final TemplateCell cell;
    private final Optional<String> bindColumn;

    /**
     * Create new region cell with characteristics based on supplied template cell, that might be populated from bind
     * column if one is supplied
     *
     * @param columnIndex
     * @param cell
     * @param bindColumn
     */
    TemplateCellWithBind(int columnIndex, TemplateCell cell, Optional<String> bindColumn) {
        this.columnIndex = columnIndex;
        this.cell = Objects.requireNonNull(cell);
        this.bindColumn = Objects.requireNonNull(bindColumn);
    }

    @Override
    public int getColumnIndex() {
        return cell.getColumnIndex();
    }

    @Override
    public CellType getCellType() {
        return cell.getCellType();
    }

    @Override
    public String getCellFormula() {
        return cell.getCellFormula();
    }

    @Override
    public String getStringCellValue() {
        return cell.getStringCellValue();
    }

    @Override
    public double getNumericCellValue() {
        return cell.getNumericCellValue();
    }

    @Override
    public boolean getBooleanCellValue() {
        return cell.getBooleanCellValue();
    }

    @Override
    public byte getErrorCellValue() {
        return cell.getErrorCellValue();
    }

    @Override
    public Comment getCellComment() {
        return cell.getCellComment();
    }

    @Override
    public Hyperlink getHyperlink() {
        return cell.getHyperlink();
    }

    @Override
    public Optional<String> getBindColumn() {
        return bindColumn;
    }
}
