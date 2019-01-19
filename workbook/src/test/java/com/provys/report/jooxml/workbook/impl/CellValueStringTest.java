package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueStringTest {

    @Test
    void getCellTypeTest() {
        assertThat(CellValueString.of("test").getCellType()).isEqualTo(CellType.STRING);
    }

    @Nonnull
    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{CellValueString.of(null), "CellValueString{value=null}"}
                , new Object[]{CellValueString.of("test"), "CellValueString{value=\"test\"}"}
        );
    }
    @ParameterizedTest
    @MethodSource
    void toStringTest(CellValueString cellValue, String result) {
        assertThat(cellValue.toString()).isEqualTo(result);
    }
}