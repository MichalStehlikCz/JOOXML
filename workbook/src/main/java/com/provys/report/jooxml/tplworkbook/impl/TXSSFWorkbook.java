package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.tplworkbook.TplSheet;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellCoordinatesFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * TXSSFWorkbook is wrapper for XSFFSheet that provides minimalistic surface, needed to provide access to information
 * being modified by JOOXML report as defined by TemplateWorkbook interface
 * It is not intended to be generic library for work with Excel file like POI and it is intentionally read-only, as
 * JOOXML report should read template, but it should not modify it
 */
class TXSSFWorkbook implements TplWorkbook {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());

    private final CellCoordinatesFactory cellCoordinatesFactory;
    private final Workbook workbook;
    private final List<TplSheet> sheets;

    TXSSFWorkbook(InputStream stream, CellCoordinatesFactory cellCoordinatesFactory) throws IOException {
        this.cellCoordinatesFactory = Objects.requireNonNull(cellCoordinatesFactory);
        this.workbook = new XSSFWorkbook(stream);
        this.sheets = initSheets(this.workbook);
    }

    @Nonnull
    private List<TplSheet> initSheets(Workbook workbook) {
        var result = new ArrayList<TplSheet>(workbook.getNumberOfSheets());
        for (Sheet sheet : workbook) {
            result.add(new TXSSFSheet(this, sheet));
        }
        return result;
    }

    /**
     * @return value of field cellCoordinatesFactory
     */
    @Nonnull
    CellCoordinatesFactory getCellCoordinatesFactory() {
        return cellCoordinatesFactory;
    }

    @Override
    @Nonnull
    public TplSheet getSheetAt(int i) {
        return sheets.get(i);
    }

    @Override
    @Nonnull
    public TplSheet getSheet() {
        return getSheetAt(0);
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    @Override
    @Nonnull
    public Iterator<TplSheet> iterator() {
        return sheets.listIterator();
    }
}
