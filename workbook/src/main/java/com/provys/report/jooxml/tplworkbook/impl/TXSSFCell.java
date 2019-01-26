package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.tplworkbook.TplRow;
import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

class TXSSFCell implements TplCell {

    private static final Logger LOG = LogManager.getLogger(TXSSFCell.class.getName());
    @Nonnull
    private final TplRow row;
    private final int colIndex;
    @Nonnull
    private final CellValue value;
    @Nullable
    private final CellProperties properties;

    TXSSFCell(TplRow row, Cell cell) {
        this.row = row;
        this.colIndex = cell.getColumnIndex();
        switch (cell.getCellType()) {
            case FORMULA:
                this.value = CellValueFormula.of(cell.getCellFormula());
                break;
            case STRING:
                this.value = CellValueString.of(cell.getStringCellValue());
                break;
            case NUMERIC:
                this.value = CellValueNumeric.of(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                this.value = CellValueBoolean.of(cell.getBooleanCellValue());
                break;
            case ERROR:
                this.value = CellValueError.of(cell.getErrorCellValue());
                break;
            case BLANK:
                this.value = CellValueBlank.get();
                break;
            default:
                LOG.warn("TXSSFCell: Unexpected cell type in base template {}", cell.getCellType());
                this.value = CellValueBlank.get();
        }
        Integer styleIndex = ((cell.getCellStyle() == null) ? null : (int) cell.getCellStyle().getIndex());
        org.apache.poi.ss.usermodel.Comment cellComment = cell.getCellComment();
        if (styleIndex != null) {
            properties = CellPropertiesInt.of(styleIndex);
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

    @Nonnull
    @Override
    public CellCoordinates getCoordinates() {
        return CellCoordinatesFactoryImpl.getInstance().of(getRowIndex(), getColIndex());
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return value.getCellType();
    }

    @Override
    @Nonnull
    public CellValue getCellValue() {
        return value;
    }

    @Override
    @Nonnull
    public Optional<CellProperties> getCellProperties() {
        return Optional.ofNullable(properties);
    }

}
