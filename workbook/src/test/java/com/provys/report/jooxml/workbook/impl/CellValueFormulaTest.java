package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueFormulaTest {

    @Nonnull
    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[] {"SUM(A1:A10)", null}
                , new Object[] {"", IllegalArgumentException.class}
                , new Object[] {null, NullPointerException.class});
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(String formula, Class<Throwable> exception) {
        if (exception == null) {
            assertThatCode(() -> CellValueFormula.of(formula)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> CellValueFormula.of(formula)).isInstanceOf(exception);
        }
    }

    @Test
    void getCellTypeTest() {
        assertThat(CellValueFormula.of("SUM(A1:A10").getCellType()).isEqualTo(CellType.FORMULA);
    }

    static Stream<Object[]> getFormulaTest() {
        return Stream.of(
                new Object[]{CellValueFormula.of("SUM(A1:A10)"), "SUM(A1:A10)"}
                , new Object[]{CellValueFormula.of("test"), "test"});
    }

    @ParameterizedTest
    @MethodSource
    void getFormulaTest(CellValueFormula cellValue, String formula) {
        assertThat(cellValue.getFormula()).isEqualTo(formula);
    }

    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[] {CellValueFormula.of("SUM(A1:A10)"), CellValueFormula.of("SUM(A1:A10)"), Boolean.TRUE}
                , new Object[] {CellValueFormula.of("SUM(A1:A10)"), CellValueFormula.of(" SUM(A1:A10)"), Boolean.FALSE}
                , new Object[] {CellValueFormula.of("SUM(A1:A10)"), CellValueFormula.of("SUM(A1:A10) "), Boolean.FALSE}
                , new Object[] {CellValueFormula.of("SUM(A1:A10)"), CellValueFormula.of("sum(A1:A10) "), Boolean.FALSE}
                , new Object[] {CellValueFormula.of("SUM(A1:A10)"), "SUM(A1:A10) ", Boolean.FALSE}
                , new Object[] {CellValueFormula.of("SUM(A1:A10)"), null, Boolean.FALSE});
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellValueFormula cellValue, Object others, Boolean result) {
        assertThat(cellValue.equals(others)).isEqualTo(result);
    }

    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[] {CellValueFormula.of("SUM(A1:A10)"), CellValueFormula.of("SUM(A1:A10)")}
                , new Object[] {CellValueFormula.of("test"), CellValueFormula.of("test")});
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
                new Object[] {CellValueFormula.of("SUM(A1:A10)"), "CellValueFormula{formula=\"SUM(A1:A10)\"}"}
                , new Object[] {CellValueFormula.of("test"), "CellValueFormula{formula=\"test\"}"});
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(CellValueFormula cellValue, String result) {
        assertThat(cellValue.toString()).isEqualTo(result);
    }
}