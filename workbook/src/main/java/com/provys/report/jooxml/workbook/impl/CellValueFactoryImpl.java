package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellValueFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of CellValueFactory interface, gives access to new instances of CellValues of various types
 */
@ApplicationScoped
public class CellValueFactoryImpl implements CellValueFactory {

    @Nonnull
    @Override
    public CellValue ofFormula(String formula) {
        return CellValueFormula.of(formula);
    }

    @Nonnull
    @Override
    public CellValue ofString(@Nullable String value) {
        return CellValueString.of(value);
    }

    @Nonnull
    @Override
    public CellValue ofNumeric(@Nullable Double value) {
        return CellValueNumeric.of(value);
    }

    @Nonnull
    @Override
    public CellValue ofBoolean(@Nullable Boolean value) {
        return CellValueBoolean.of(value);
    }

    @Nonnull
    @Override
    public CellValue ofError(@Nullable Byte value) {
        return CellValueError.of(value);
    }

    @Nonnull
    @Override
    public CellValue getBlank() {
        return CellValueBlank.get();
    }
}
