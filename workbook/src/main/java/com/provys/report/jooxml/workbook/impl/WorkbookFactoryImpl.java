package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.Comment;
import com.provys.report.jooxml.workbook.WorkbookFactoryInt;
import org.jetbrains.annotations.Nullable;

public class WorkbookFactoryImpl implements WorkbookFactoryInt {
    @Override
    public CellValue getFormulaValue(String formula) {
        return new CellValueFormula(formula);
    }

    @Override
    public CellValue getStringValue(@Nullable String value) {
        return null;
    }

    @Override
    public CellValue getNumericValue(@Nullable Double value) {
        return null;
    }

    @Override
    public CellValue getBooleanValue(@Nullable Boolean value) {
        return null;
    }

    @Override
    public CellValue getErrorValue(@Nullable Byte value) {
        return null;
    }

    @Override
    public CellValue getBlankValue() {
        return null;
    }

    @Override
    public CellProperties getProperties(@Nullable Integer styleIndex, @Nullable Comment comment) {
        return new CellPropertiesImpl(styleIndex, comment);
    }

    @Override
    public Comment getComment(boolean visible, @Nullable String author, @Nullable String text) {
        return null;
    }

}
