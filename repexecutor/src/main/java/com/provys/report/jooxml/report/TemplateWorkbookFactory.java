package com.provys.report.jooxml.report;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public interface TemplateWorkbookFactory {
    TemplateWorkbook get(File template) throws IOException, InvalidFormatException;
}
