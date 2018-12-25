package com.provys.jooxml.repexecutor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public interface RepWorkbookFactory {
    RepWorkbook get(File template) throws IOException, InvalidFormatException;
}
