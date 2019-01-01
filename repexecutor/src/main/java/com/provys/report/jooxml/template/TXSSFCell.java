package com.provys.report.jooxml.template;

import com.provys.report.jooxml.report.TemplateCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Optional;

public class TXSSFCell implements TemplateCell {
    private final Cell cell;

    public TXSSFCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public int getRowIndex() {
        return cell.getRowIndex();
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
        return Optional.ofNullable(cell.getStringCellValue());
    }

    @Override
    public Optional<Double> getNumericCellValue() {
        return Optional.of(cell.getNumericCellValue());
    }

    @Override
    public Optional<Boolean> getBooleanCellValue() {
        return Optional.of(cell.getBooleanCellValue());
    }

    @Override
    public Optional<Byte> getErrorCellValue() {
        return Optional.of(cell.getErrorCellValue());
    }

    @Override
    public int getStyleIndex() {
        return (cell.getCellStyle() == null) ? -1 : cell.getCellStyle().getIndex();
    }

    @Override
    public Comment getCellComment() {
        return cell.getCellComment();
    }

    @Override
    public Hyperlink getHyperlink() {
        return cell.getHyperlink();
    }
}
