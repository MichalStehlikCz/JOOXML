package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueIntTest {

    @Test
    void ofFormulaTest() {
        CellValueInt value = CellValueInt.ofFormula("SUM(A1:A10)");
        assertThat(value.getCellType()).isEqualTo(CellType.FORMULA);
    }

    @Test
    void ofStringTest() {
        CellValueInt value = CellValueInt.ofString("Test");
        assertThat(value.getCellType()).isEqualTo(CellType.STRING);
    }

    @Test
    void ofNumericTest() {
        CellValueInt value = CellValueInt.ofNumeric(15.0);
        assertThat(value.getCellType()).isEqualTo(CellType.NUMERIC);
    }

    @Test
    void ofBooleanTest() {
        CellValueInt value = CellValueInt.ofBoolean(true);
        assertThat(value.getCellType()).isEqualTo(CellType.BOOLEAN);
    }

    @Test
    void ofErrorTest() {
        CellValueInt value = CellValueInt.ofError((byte) 5);
        assertThat(value.getCellType()).isEqualTo(CellType.ERROR);
    }

    @Test
    void ofBlankTest() {
        CellValueInt value = CellValueInt.ofBlank();
        assertThat(value.getCellType()).isEqualTo(CellType.BLANK);
    }
}