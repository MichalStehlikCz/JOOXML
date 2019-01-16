package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueBooleanTest {

    @Nonnull
    static Stream<Object[]> getCellTypeTest() {
        return Stream.of(
                new Object[]{CellValueBoolean.of(true)}
                , new Object[]{CellValueBoolean.of(false)}
                , new Object[]{CellValueBoolean.of(null)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getCellTypeTest(CellValueBoolean value) {
        assertThat(value.getCellType()).isEqualTo(CellType.BOOLEAN);
    }

    @Nonnull
    static Stream<Object[]> getValueTest() {
        return Stream.of(
                new Object[]{CellValueBoolean.of(true), Boolean.TRUE}
                , new Object[]{CellValueBoolean.of(false), Boolean.FALSE}
                , new Object[]{CellValueBoolean.of(null), null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getValueTest(CellValueBoolean value, @Nullable Boolean result) {
        assertThat(value.getValue()).isEqualTo(Optional.ofNullable(result));
    }
}