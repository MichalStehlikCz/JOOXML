package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellCoordinatesFactoryImplTest {

    @Nonnull
    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{0, 0}
                , new Object[]{6, 26}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(int row, int col) {
        var factory = new CellCoordinatesFactoryImpl();
        var coordinates = factory.of(row, col);
        assertThat(coordinates.getRow()).isEqualTo(row);
        assertThat(coordinates.getCol()).isEqualTo(col);
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", 0, 0}
                , new Object[]{"AA7", 6, 26}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String address, int row, int col) {
        var factory = new CellCoordinatesFactoryImpl();
        var coordinates = factory.parse(address);
        assertThat(coordinates.getRow()).isEqualTo(row);
        assertThat(coordinates.getCol()).isEqualTo(col);
    }

}