package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellPropertiesIntTest {

    @Test
    void getStyleIndexTest() {
        assertThat(CellPropertiesInt.of(5).getStyleIndex()).isEqualTo(Optional.of(5));
        assertThat(CellPropertiesInt.of(null).getStyleIndex()).isEqualTo(Optional.empty());
    }

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{CellPropertiesInt.of(3), null, false}
                , new Object[]{CellPropertiesInt.of(3), 3, false}
                , new Object[]{CellPropertiesInt.of(3), CellPropertiesInt.of(5), false}
                , new Object[]{CellPropertiesInt.of(3), CellPropertiesInt.of(null), false}
                , new Object[]{CellPropertiesInt.of(3), CellPropertiesInt.of(3), true}
                , new Object[]{CellPropertiesInt.of(null), CellPropertiesInt.of(5), false}
                , new Object[]{CellPropertiesInt.of(null), CellPropertiesInt.of(null), true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellPropertiesInt properties, @Nullable Object other, boolean result) {
        assertThat(properties.equals(other)).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("equalsTest")
    void hashCodeTest(CellPropertiesInt properties, @Nullable Object other, boolean result) {
        if (result) {
            assertThat(properties.hashCode()).isEqualTo(other.hashCode());
        }
    }

    @Test
    void toStringTest() {
        assertThat(CellPropertiesInt.of(5).toString()).isEqualTo("CellPropertiesInt{styleIndex=5}");
        assertThat(CellPropertiesInt.of(null).toString()).isEqualTo("CellPropertiesInt{styleIndex=null}");
    }
}