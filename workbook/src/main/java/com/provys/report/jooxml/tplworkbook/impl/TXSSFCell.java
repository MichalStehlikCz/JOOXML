package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.impl.Workbooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Optional;

class TXSSFCell implements TplCell {

    private static final Logger LOG = LogManager.getLogger(TXSSFCell.class.getName());
    private final TplRow row;
    private final int colIndex;
    private final CellValue value;
    private final CellProperties properties;

    public TXSSFCell(TplRow row, Cell cell) {
        this.row = row;
        this.colIndex = cell.getColumnIndex();
        switch (cell.getCellType()) {
            case FORMULA:
                this.value = Workbooks.getFormulaValue(cell.getCellFormula());
                break;
            case STRING:
                this.value = Workbooks.getStringValue(cell.getStringCellValue());
                break;
            case NUMERIC:
                this.value = Workbooks.getNumericValue(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                this.value = Workbooks.getBooleanValue(cell.getBooleanCellValue());
                break;
            case ERROR:
                this.value = Workbooks.getErrorValue(cell.getErrorCellValue());
                break;
            case BLANK:
                this.value = Workbooks.getBlankValue();
                break;
            default:
                LOG.warn("TXSSFCell: Unexpected cell type in base template {}", cell.getCellType());
                this.value = Workbooks.getBlankValue();
        }
        Integer styleIndex = ((cell.getCellStyle() == null) ? null : (int) cell.getCellStyle().getIndex());
        org.apache.poi.ss.usermodel.Comment cellComment = cell.getCellComment();
        if (styleIndex != null) {
            properties = Workbooks.getProperties(styleIndex);
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
