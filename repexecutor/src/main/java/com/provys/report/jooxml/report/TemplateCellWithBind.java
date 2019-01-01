package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.DataRecord;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell from template workbook. Has coordinates relative to its parent region and holds reference to
 * bind column if one exists for the cell.
 */
class TemplateCellWithBind implements AreaCell {

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
        if ((cell.getCellType() == CellType.FORMULA) && (bindColumn.isPresent())) {
            throw new IllegalArgumentException("Value binding to formula cell is not allowed");
        }
        if ((cell.getCellType() == CellType.ERROR) && (bindColumn.isPresent())) {
            throw new IllegalArgumentException("Value binding to error cell is not allowed");
        }
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
    public Optional<String> getStringCellValue() {
        return cell.getStringCellValue();
    }

    @Override
    public Optional<Double> getNumericCellValue() {
        return cell.getNumericCellValue();
    }

    @Override
    public Optional<Boolean> getBooleanCellValue() {
        return cell.getBooleanCellValue();
    }

    @Override
    public Optional<Byte> getErrorCellValue() {
        return cell.getErrorCellValue();
    }

    @Override
    public Optional<String> getStringEffectiveValue(DataRecord data) {
        return bindColumn.map((column) -> data.getStringValue(column)).orElse(getStringCellValue());
    }

    @Override
    public Optional<Double> getNumericEffectiveValue(DataRecord data) {
        return bindColumn.map((column) -> data.getNumericValue(column)).orElse(getNumericCellValue());
    }

    @Override
    public Optional<Boolean> getBooleanEffectiveValue(DataRecord data) {
        return bindColumn.map((column) -> data.getBooleanValue(column)).orElse(getBooleanCellValue());
    }

    @Override
    public int getCellStyleIndex() {
        return cell.getStyleIndex();
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
