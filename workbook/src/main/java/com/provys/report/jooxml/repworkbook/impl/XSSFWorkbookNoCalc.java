package com.provys.report.jooxml.repworkbook.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

/**
 * Class is used internally by RXSSFWorkbook to model all parts of sheet except of sheetData.
 * Cannot use XSSFWorkbook directly, as it does not expose methods that would allow to remove CalculationChain
 * and we have to remove it as cells are not exported to resulting xlsx file
 */
class XSSFWorkbookNoCalc extends XSSFWorkbook {

    XSSFWorkbookNoCalc(File file) throws IOException, InvalidFormatException {
        super(file);
        // we want to remove calculation chain, as cells will no be use
        removeRelation(getCalculationChain());
    }
}
