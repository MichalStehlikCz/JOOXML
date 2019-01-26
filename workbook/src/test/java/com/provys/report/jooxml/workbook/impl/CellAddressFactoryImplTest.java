package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellAddressFactoryImplTest {

    @Nonnull
    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{null, CellCoordinatesInt.of(0, 0)}
                , new Object[]{"Sheet", CellCoordinatesInt.of(0, 0)}
                , new Object[]{"Sheet", CellCoordinatesInt.of(5, 4)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(@Nullable String sheetName, CellCoordinates coordinates) {
        var factory = new CellAddressFactoryImpl();
        var address = factory.of(sheetName, coordinates);
        assertThat(address.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
        assertThat(address.getCoordinates()).isEqualTo(coordinates);
    }

    @Nonnull
    static Stream<Object[]> of1Test() {
        return Stream.of(
                new Object[]{CellCoordinatesInt.of(0, 0)}
                , new Object[]{CellCoordinatesInt.of(0, 0)}
                , new Object[]{CellCoordinatesInt.of(5, 4)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void of1Test(CellCoordinates coordinates) {
        var factory = new CellAddressFactoryImpl();
        var address = factory.of(coordinates);
        assertThat(address.getSheetName()).isEqualTo(Optional.empty());
        assertThat(address.getCoordinates()).isEqualTo(coordinates);
    }

    @Nonnull
    static Stream<Object[]> of2Test() {
        return Stream.of(
                new Object[]{null, 0, 0}
                , new Object[]{"Sheet", 0, 0}
                , new Object[]{"Sheet", 6, 26}
        );
    }

    @ParameterizedTest
    @MethodSource
    void of2Test(@Nullable String sheetName, int row, int col) {
        var factory = new CellAddressFactoryImpl();
        var address = factory.of(sheetName, row, col);
        assertThat(address.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
        assertThat(address.getRow()).isEqualTo(row);
        assertThat(address.getCol()).isEqualTo(col);
    }

    @Nonnull
    static Stream<Object[]> of3Test() {
        return Stream.of(
                new Object[]{0, 0}
                , new Object[]{6, 26}
        );
    }

    @ParameterizedTest
    @MethodSource
    void of3Test(int row, int col) {
        var factory = new CellAddressFactoryImpl();
        var address = factory.of(row, col);
        assertThat(address.getSheetName()).isEqualTo(Optional.empty());
        assertThat(address.getRow()).isEqualTo(row);
        assertThat(address.getCol()).isEqualTo(col);
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", null, 0, 0}
        , new Object[]{"Sheet1!A1", "Sheet1", 0, 0}
                , new Object[]{"AA7", null, 6, 26}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String address, @Nullable String sheetName, int row, int col) {
        var factory = new CellAddressFactoryImpl();
        var cellAddress = factory.parse(address);
        assertThat(cellAddress.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
        assertThat(cellAddress.getRow()).isEqualTo(row);
        assertThat(cellAddress.getCol()).isEqualTo(col);
    }

}