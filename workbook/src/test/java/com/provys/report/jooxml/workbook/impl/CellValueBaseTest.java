package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellValueBaseTest {

    static private class CellValueString extends CellValueBase<String> {

        CellValueString(@Nullable String value) {
            super(value);
        }

        @Nonnull
        @Override
        public CellType getCellType() {
            return CellType.STRING;
        }
    }

    @Nonnull
    static Stream<Object[]> getValueTest() {
        return Stream.of(
                new Object[]{new CellValueString(null), null}
                , new Object[]{new CellValueString("test"), "test"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getValueTest(CellValueString value, String result) {
        assertThat(value.getValue()).isEqualTo(Optional.ofNullable(result));
    }

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{new CellValueString(null), new CellValueString(null), true}
                , new Object[]{new CellValueString("test"), new CellValueString("test"), true}
                , new Object[]{new CellValueString(null), new CellValueString("test"), false}
                , new Object[]{new CellValueString("test"), new CellValueString(null), false}
                , new Object[]{new CellValueString("test"), new CellValueString("test1"), false}
                , new Object[]{new CellValueString("test"), null, false}
                , new Object[]{new CellValueString("test"), "test", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellValueString value, @Nullable Object other, boolean result) {
        assertThat(value.equals(other)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[]{new CellValueString(null), new CellValueString(null)}
                , new Object[]{new CellValueString("test"), new CellValueString("test")}
        );
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(CellValueString value, CellValueString other) {
        assertThat(value.hashCode()).isEqualTo(other.hashCode());
    }

    @Nonnull
    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{new CellValueString(null), "CellValueString{value=null}"}
                , new Object[]{new CellValueString("test"), "CellValueString{value=test}"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(CellValueString value, String result) {
        assertThat(value.toString()).isEqualTo(result);
    }
}