package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.*;
import com.provys.report.jooxml.workbook.impl.CellPropertiesInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

class TXSSFCell implements TplCell {

    private static final Logger LOG = LogManager.getLogger(TXSSFCell.class);
    @Nonnull
    private final TXSSFRow row;
    private final int colIndex;
    @Nonnull
    private final CellValue value;
    @Nullable
    private final CellProperties properties;

    TXSSFCell(TXSSFRow row, Cell cell) {
        this.row = Objects.requireNonNull(row);
        this.colIndex = cell.getColumnIndex();
        var cellValueFactory = row.getSheet().getWorkbook().getCellValueFactory();
        switch (cell.getCellType()) {
            case FORMULA:
                this.value = cellValueFactory.ofFormula(cell.getCellFormula());
                break;
            case STRING:
                this.value = cellValueFactory.ofString(cell.getStringCellValue());
                break;
            case NUMERIC:
                this.value = cellValueFactory.ofNumeric(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                this.value = cellValueFactory.ofBoolean(cell.getBooleanCellValue());
                break;
            case ERROR:
                this.value = cellValueFactory.ofError(cell.getErrorCellValue());
                break;
            case BLANK:
                this.value = cellValueFactory.getBlank();
                break;
            default:
                LOG.warn("TXSSFCell: Unexpected cell type in base template {}", cell.getCellType());
                this.value = cellValueFactory.getBlank();
        }
        Integer styleIndex = ((cell.getCellStyle() == null) ? null : (int) cell.getCellStyle().getIndex());
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
        return row.getSheet().getWorkbook().getCellCoordinatesFactory().of(getRowIndex(), getColIndex());
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

    @Nonnull
    @Override
    public Map<String, CellReference> getReferenceMap() {
        if (value.getCellType() != CellType.FORMULA) {
            return Collections.emptyMap();
        }
        var cellReferenceFactory = row.getSheet().getWorkbook().getCellReferenceFactory();
        var cellReferencePattern = Pattern.compile(cellReferenceFactory.getRegex());
        var matcher = cellReferencePattern.matcher(value.getFormula());
        Map<String, CellReference> referenceMap = null;
        while (matcher.find()) {
            if (referenceMap == null) {
                referenceMap = new HashMap<>();
            }
            referenceMap.computeIfAbsent(matcher.group(0), cellReferenceFactory::parse);
        }
        return (referenceMap == null) ? Collections.emptyMap() : referenceMap;
    }
}
