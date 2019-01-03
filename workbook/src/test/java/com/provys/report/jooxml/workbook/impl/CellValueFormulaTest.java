package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueFormulaTest {

    static Stream<Object[]> newTest() {
        return Stream.of(
                new Object[] {"SUM(A1:A10)", null}
                , new Object[] {"", null, IllegalArgumentException.class}
                , new Object[] {null, "VALUE", NullPointerException.class});
    }

    @ParameterizedTest
    @MethodSource
    void newTest(String formula, Class<Throwable> exception) {
        if (exception == null) {
            assertThatCode(() -> new CellValueFormula(formula)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> new CellValueFormula(formula)).isInstanceOf(exception);
        }
    }

    @Test
    void getCellTypeTest() {
        assertThat(new CellValueFormula("SUM(A1:A10").getCellType()).isEqualTo(CellType.FORMULA);
    }

    static Stream<Object[]> getFormulaTest() {
        return Stream.of(
                new Object[]{new CellValueFormula("SUM(A1:A10)"), "SUM(A1:A10)"}
                , new Object[]{new CellValueFormula("test"), "test"});
    }

    @ParameterizedTest
    @MethodSource
    void getFormulaTest(CellValueFormula cellValue, String formula) {
        assertThat(cellValue.getFormula()).isEqualTo(formula);
    }

    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[] {new CellValueFormula("SUM(A1:A10)"), new CellValueFormula("SUM(A1:A10)"), Boolean.TRUE}
                , new Object[] {new CellValueFormula("SUM(A1:A10)"), new CellValueFormula(" SUM(A1:A10)"), Boolean.FALSE}
                , new Object[] {new CellValueFormula("SUM(A1:A10)"), new CellValueFormula("SUM(A1:A10) "), Boolean.FALSE}
                , new Object[] {new CellValueFormula("SUM(A1:A10)"), new CellValueFormula("sum(A1:A10) "), Boolean.FALSE}
                , new Object[] {new CellValueFormula("SUM(A1:A10)"), "SUM(A1:A10) ", Boolean.FALSE}
                , new Object[] {new CellValueFormula("SUM(A1:A10)"), null, Boolean.FALSE});
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellValueFormula cellValue, Object others, Boolean result) {
        assertThat(cellValue.equals(others)).isEqualTo(result);
    }

    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[] {new CellValueFormula("SUM(A1:A10)"), new CellValueFormula("SUM(A1:A10)")}
                , new Object[] {new CellValueFormula("test"), new CellValueFormula("test")});
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(CellValueFormula cellValue, CellValueFormula other) {
        if (cellValue.equals(other)) {
            assertThat(cellValue.hashCode()).isEqualTo(other.hashCode());
        }
    }

    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[] {new CellValueFormula("SUM(A1:A10)"), "CellValueFormula{formula=\"SUM(A1:A10)\"}"}
                , new Object[] {new CellValueFormula("test"), "CellValueFormula{formula=\"test\"}"});
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(CellValueFormula cellValue, String result) {
        assertThat(cellValue.toString()).isEqualTo(result);
    }
}