package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CellValueFactoryImplTest {

    @Test
    void ofFormulaTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.ofFormula("SUM(X1:X10)");
        assertThat(value.getCellType()).isEqualTo(CellType.FORMULA);
        assertThat(value.getFormula()).isEqualTo("SUM(X1:X10)");
    }

    @Test
    void ofStringTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.ofString("Test string");
        assertThat(value.getCellType()).isEqualTo(CellType.STRING);
        assertThat(value.getStringValue()).isEqualTo(Optional.of("Test string"));
    }

    @Test
    void ofNumericTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.ofNumeric(15.5);
        assertThat(value.getCellType()).isEqualTo(CellType.NUMERIC);
        assertThat(value.getNumericValue()).isEqualTo(Optional.of(15.5));
    }

    @Test
    void ofBooleanTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.ofBoolean(true);
        assertThat(value.getCellType()).isEqualTo(CellType.BOOLEAN);
        assertThat(value.getBooleanValue()).isEqualTo(Optional.of(true));
    }

    @Test
    void ofErrorTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.ofError((byte) 10);
        assertThat(value.getCellType()).isEqualTo(CellType.ERROR);
        assertThat(value.getErrorValue()).isEqualTo(Optional.of((byte) 10));
    }

    @Test
    void getBlankTest() {
        var factory = new CellValueFactoryImpl();
        CellValue value = factory.getBlank();
        assertThat(value.getCellType()).isEqualTo(CellType.BLANK);
    }
}