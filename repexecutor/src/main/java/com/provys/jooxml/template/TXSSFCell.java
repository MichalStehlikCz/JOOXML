package com.provys.jooxml.template;

import com.provys.jooxml.report.TemplateCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

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
