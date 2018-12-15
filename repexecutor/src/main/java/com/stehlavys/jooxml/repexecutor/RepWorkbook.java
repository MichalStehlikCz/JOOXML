package com.stehlavys.jooxml.repexecutor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class RepWorkbook extends XSSFWorkbook {
    public RepWorkbook(File file) throws IOException, InvalidFormatException {
        super(file);
    }
}
