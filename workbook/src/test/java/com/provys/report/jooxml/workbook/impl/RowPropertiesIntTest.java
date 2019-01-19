package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class RowPropertiesIntTest {

    @Nonnull
    static Stream<Object[]> getCellTypeTest() {
        return Stream.of(
                new Object[]{10, true, 1, null}
                , new Object[]{-1, false, 1, IllegalArgumentException.class}
                , new Object[]{3, true, -1, IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(float heightInPoints, boolean hidden, int styleIndex, @Nullable Class<Throwable> ex) {
        if (ex == null) {
            assertThatCode(() -> RowPropertiesInt.of(heightInPoints, hidden, styleIndex)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> RowPropertiesInt.of(heightInPoints, hidden, styleIndex)).isInstanceOf(ex);
        }
    }

    @Test
    void getHeightInPointsTest() {
        assertThat(RowPropertiesInt.of(5.1f, false, 15).getHeightInPoints()).isEqualTo(5.1f);
    }

    @Test
    void isHiddenTest() {
        assertThat(RowPropertiesInt.of(5.1f, false, 15).isHidden()).isEqualTo(false);
    }

    @Test
    void getStyleIndexTest() {
        assertThat(RowPropertiesInt.of(5.1f, false, 15).getStyleIndex()).isEqualTo(15);
    }

    @Test
    void name() {
    }
}