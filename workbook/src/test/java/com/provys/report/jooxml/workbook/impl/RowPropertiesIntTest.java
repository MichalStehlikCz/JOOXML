package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class RowPropertiesIntTest {

    @Nonnull
    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{10f, true, (short) 1, null}
                , new Object[]{null, false, (short) 1, null}
                , new Object[]{10f, true, null, null}
                , new Object[]{-1f, false, (short) 1, IllegalArgumentException.class}
                , new Object[]{3f, true, (short) -1, IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(@Nullable Float heightInPoints, boolean hidden, @Nullable Short styleIndex, @Nullable Class<Throwable> ex) {
        if (ex == null) {
            assertThatCode(() -> RowPropertiesInt.of(heightInPoints, hidden, styleIndex)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> RowPropertiesInt.of(heightInPoints, hidden, styleIndex)).isInstanceOf(ex);
        }
    }

    @Nonnull
    static Stream<Object[]> getHeightInPointsTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1), 10f}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1), null}
                , new Object[]{RowPropertiesInt.of(10f, true, null), 10f}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getHeightInPointsTest(RowPropertiesInt properties, @Nullable Float heightInPoints) {
        assertThat(properties.getHeightInPoints()).isEqualTo(Optional.ofNullable(heightInPoints));
    }

    @Nonnull
    static Stream<Object[]> isHiddenTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1), true}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1), false}
                , new Object[]{RowPropertiesInt.of(10f, true, null), true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isHiddenTest(RowPropertiesInt properties, boolean hidden) {
        assertThat(properties.isHidden()).isEqualTo(hidden);
    }

    @Nonnull
    static Stream<Object[]> getStyleIndexTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1), (short) 1}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1), (short) 1}
                , new Object[]{RowPropertiesInt.of(10f, true, null), null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStyleIndexTest(RowPropertiesInt properties, @Nullable Short styleIndex) {
        assertThat(properties.getStyleIndex()).isEqualTo(Optional.ofNullable(styleIndex));
    }

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(10f, true, (short) 1), true}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1),
                        RowPropertiesInt.of(null, false, (short) 1), true}
                , new Object[]{RowPropertiesInt.of(10f, true, null),
                        RowPropertiesInt.of(10f, true, null), true}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(11f, false, (short) 1), false}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(10f, false, (short) 2), false}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(null, false, (short) 1), false}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(10f, false, null), false}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1), null, false}
                , new Object[]{RowPropertiesInt.of(10f, true, (short) 1), 10f, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(RowPropertiesInt properties, Object other, Boolean result) {
        assertThat(properties.equals(other)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        RowPropertiesInt.of(10f, true, (short) 1)}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1),
                        RowPropertiesInt.of(null, false, (short) 1)}
                , new Object[]{RowPropertiesInt.of(10f, true, null),
                        RowPropertiesInt.of(10f, true, null)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(RowPropertiesInt properties, RowPropertiesInt other) {
        assertThat(properties.hashCode()).isEqualTo(other.hashCode());
    }

    @Nonnull
    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{RowPropertiesInt.of(10f, true, (short) 1),
                        "RowPropertiesInt{heightInPoints=10.0, hidden=true, styleIndex=1}"}
                , new Object[]{RowPropertiesInt.of(null, false, (short) 1),
                        "RowPropertiesInt{heightInPoints=null, hidden=false, styleIndex=1}"}
                , new Object[]{RowPropertiesInt.of(10f, true, null),
                        "RowPropertiesInt{heightInPoints=10.0, hidden=true, styleIndex=null}"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(RowPropertiesInt properties, String result) {
        assertThat(properties.toString()).isEqualTo(result);
    }
}