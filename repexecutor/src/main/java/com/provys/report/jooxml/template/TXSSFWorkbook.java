package com.provys.report.jooxml.template;

import com.provys.report.jooxml.report.TemplateSheet;
import com.provys.report.jooxml.report.TemplateWorkbook;
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
public class TXSSFWorkbook implements TemplateWorkbook {
    private final Workbook workbook;
    final private List<TemplateSheet> sheets;

    private static List<TemplateSheet> initSheets(Workbook workbook) {
        List<TemplateSheet> result = new ArrayList(workbook.getNumberOfSheets());
        for (Sheet sheet : workbook) {
            result.add(new TXSSFSheet(sheet));
        }
        return result;
    }

    public TXSSFWorkbook(File file) throws IOException, InvalidFormatException {
        workbook = new XSSFWorkbook(file);
        sheets = initSheets(workbook);
    }

    @Override
    public TemplateSheet getSheetAt(int i) {
        return sheets.get(i);
    }

    @Override
    public TemplateSheet getSheet() {
        return getSheetAt(0);
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    @Override
    public Iterator<TemplateSheet> iterator() {
        return sheets.listIterator();
    }
}
