package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.repexecutor.RepExecutor;
import com.provys.report.jooxml.tplworkbook.TplSheet;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TXSSFWorkbook is wrapper for XSFFSheet that provides minimalistic surface, needed to provide access to information
 * being modified by JOOXML report as defined by TemplateWorkbook interface
 * It is not intended to be generic library for work with Excel file like POI and it is intentionally read-only, as
 * JOOXML report should read template, but it should not modify it
 */
class TXSSFWorkbook implements TplWorkbook {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());

    private final Workbook workbook;
    final private List<TplSheet> sheets;

    private static List<TplSheet> initSheets(Workbook workbook) {
        var result = new ArrayList<TplSheet>(workbook.getNumberOfSheets());
        for (Sheet sheet : workbook) {
            result.add(new TXSSFSheet(sheet));
        }
        return result;
    }

    TXSSFWorkbook(File file) throws IOException {
        try {
            workbook = new XSSFWorkbook(file);
        } catch (InvalidFormatException e) {
            LOG.error("OpenTemplate: invalid format exception opening template {} {}", file.getPath(), e);
            throw new RuntimeException("OpenTemplate: invalid format exception opening template", e);
        }
        sheets = initSheets(workbook);
    }

    @Override
    public TplSheet getSheetAt(int i) {
        return sheets.get(i);
    }

    @Override
    public TplSheet getSheet() {
        return getSheetAt(0);
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    @Override
    public Iterator<TplSheet> iterator() {
        return sheets.listIterator();
    }
}
