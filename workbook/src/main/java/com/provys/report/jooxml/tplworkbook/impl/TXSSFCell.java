package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.workbook.*;
import com.provys.report.jooxml.workbook.impl.WorkbookFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Optional;

public class TXSSFCell implements TplCell {

    private static final Logger LOG = LogManager.getLogger(TXSSFCell.class.getName());
    private final TplRow row;
    private final int colIndex;
    private final CellValue value;
    private final CellProperties properties;

    public TXSSFCell(TplRow row, Cell cell) {
        this.row = row;
        this.colIndex = cell.getColumnIndex();
        WorkbookFactoryInt factory = new WorkbookFactoryImpl();
        switch (cell.getCellType()) {
            case FORMULA:
                this.value = factory.getFormulaValue(cell.getCellFormula());
                break;
            default:
                LOG.warn("TXSSFCell: Unexpected cell type in base template {}", cell.getCellType());
                this.value = factory.getBlankValue();
        }
        Integer styleIndex = ((cell.getCellStyle() == null) ? null : (int) cell.getCellStyle().getIndex());
        org.apache.poi.ss.usermodel.Comment cellComment = cell.getCellComment();
        Comment comment = (cellComment == null) ? null : factory.getComment(
                cellComment.isVisible(), cellComment.getAuthor(), cellComment.getString().getString());
        if ((styleIndex != null) || (comment != null)) {
            properties = factory.getProperties(styleIndex, comment);
        } else {
            properties = null;
        }
    }

    @Override
    public int getRowIndex() {
        return row.getRowIndex();
    }

    @Override
    public int getColIndex() {
        return colIndex;
    }

    @Override
    public CellType getCellType() {
        return value.getCellType();
    }

    @Override
    public CellValue getCellValue() {
        return value;
    }

    @Override
    public Optional<CellProperties> getCellProperties() {
        return Optional.ofNullable(properties);
    }

}
