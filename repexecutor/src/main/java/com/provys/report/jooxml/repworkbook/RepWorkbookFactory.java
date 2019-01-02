package com.provys.report.jooxml.repworkbook;

import com.provys.report.jooxml.repworkbook.RepWorkbook;

import java.io.File;
import java.io.IOException;

public interface RepWorkbookFactory {
    RepWorkbook get(File template) throws IOException;
}
