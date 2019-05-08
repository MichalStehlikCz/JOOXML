package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CellAddressIntTest {

    @Nonnull
    static Stream<Object[]> getREGEXPTest() {
        return Stream.of(
                new Object[]{"A1", true}
                , new Object[]{"AA7", true}
                , new Object[]{"AB30", true}
                , new Object[]{"Sheet1!A1", true}
                , new Object[]{"My_Sheet!A1", true}
                , new Object[]{"'xx''!ab'!A1", true}
                , new Object[]{"A0", false}
                , new Object[]{"0", false}
                , new Object[]{"A", false}
                , new Object[]{"11", false}
                , new Object[]{"$A1", false}
                , new Object[]{"A$1", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getREGEXPTest(String address, boolean match) {
        var matcher = Pattern.compile(CellAddressInt.REGEX).matcher(address);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", CellAddressInt.of(0, 0), null, null}
                , new Object[]{"AA7", CellAddressInt.of(6, 26), null, null}
                , new Object[]{"AB30", CellAddressInt.of(29, 27), null, null}
                , new Object[]{"My_Sheet!A1", CellAddressInt.of("My_Sheet", 0, 0), null, null}
                , new Object[]{"'xx''!ab'!A1", CellAddressInt.of("xx'!ab", 0, 0), null, null}
                , new Object[]{"A0", null, IllegalArgumentException.class, "\"A0\""}
                , new Object[]{"0", null, IllegalArgumentException.class, "\"0\""}
                , new Object[]{"A", null, IllegalArgumentException.class, "\"A\""}
                , new Object[]{"11", null, IllegalArgumentException.class, "\"11\""}
                , new Object[]{"$A1", null, IllegalArgumentException.class, "\"$A1\""}
                , new Object[]{"A$1", null, IllegalArgumentException.class, "\"A$1\""}
                , new Object[]{null, null, NullPointerException.class, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String addressString, @Nullable CellAddressInt address, @Nullable Class<Throwable> exception,
                   @Nullable String message) {
        if (exception == null) {
            assertThat(CellAddressInt.parse(addressString)).isEqualTo(address);
        } else {
            var ex = assertThatThrownBy(() -> CellAddressInt.parse(addressString)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    @Nonnull
    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{null, CellCoordinatesInt.of(0, 0), null}
                , new Object[]{"Sheet1", CellCoordinatesInt.of(5, 15), null}
                , new Object[]{"Sheet 1", CellCoordinatesInt.of(5, 15), null}
                , new Object[]{"", CellCoordinatesInt.of(0, 0), IllegalArgumentException.class}
                , new Object[]{null, null, NullPointerException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(String sheetName, CellCoordinates coordinates, @Nullable Class<Throwable> exception) {
        if (exception == null) {
            //noinspection DuplicateExpressions
            assertThatCode(() -> CellAddressInt.of(sheetName, coordinates)).doesNotThrowAnyException();
        } else {
            //noinspection DuplicateExpressions
            assertThatThrownBy(() -> CellAddressInt.of(sheetName, coordinates)).isInstanceOf(exception);
        }
    }

    @Nonnull
    static Stream<Object[]> getSheetNameTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(null, CellCoordinatesInt.of(0, 0)), null}
                , new Object[]{CellAddressInt.of("Sheet1", CellCoordinatesInt.of(5, 15)),
                        "Sheet1"}
                , new Object[]{CellAddressInt.of("Sheet 1", CellCoordinatesInt.of(123, 15674)),
                        "Sheet 1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getSheetNameTest(CellAddressInt address, @Nullable String sheetName) {
        assertThat(address.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
    }

    @Nonnull
    static Stream<Object[]> getCoordinatesTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(null, CellCoordinatesInt.of(0, 0)),
                        CellCoordinatesInt.of(0, 0)}
                , new Object[]{CellAddressInt.of("Sheet1", CellCoordinatesInt.of(5, 15)),
                        CellCoordinatesInt.of(5, 15)}
                , new Object[]{CellAddressInt.of("Sheet 1", CellCoordinatesInt.of(123, 15674)),
                        CellCoordinatesInt.of(123, 15674)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getCoordinatesTest(CellAddressInt address, CellCoordinates coordinates) {
        assertThat(address.getCoordinates()).isEqualTo(coordinates);
    }

    @Nonnull
    static Stream<Object[]> getRowTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(null, CellCoordinatesInt.of(0, 0)), 0}
                , new Object[]{CellAddressInt.of("Sheet1", CellCoordinatesInt.of(5, 15)),5}
                , new Object[]{CellAddressInt.of("Sheet 1", CellCoordinatesInt.of(123, 15674)), 123}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getRowTest(CellAddressInt address, int row) {
        assertThat(address.getRow()).isEqualTo(row);
    }

    @Nonnull
    static Stream<Object[]> getColTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(null, CellCoordinatesInt.of(0, 0)), 0}
                , new Object[]{CellAddressInt.of("Sheet1", CellCoordinatesInt.of(5, 15)), 15}
                , new Object[]{CellAddressInt.of("Sheet 1", CellCoordinatesInt.of(123, 15674)), 15674}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getColTest(CellAddressInt address, int col) {
        assertThat(address.getCol()).isEqualTo(col);
    }

    @Nonnull
    static Stream<Object[]> appendAddressTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(0, 0), "A1"}
                , new Object[]{CellAddressInt.of(6, 26), "AA7"}
                , new Object[]{CellAddressInt.of(29, 27), "AB30"}
                , new Object[]{CellAddressInt.of("My_Sheet", 0, 0), "My_Sheet!A1"}
                , new Object[]{CellAddressInt.of("xx'!ab", 0, 0), "'xx''!ab'!A1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void appendAddressTest(CellAddressInt address, String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        address.appendAddress(builder);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }

    @Nonnull
    static Stream<Object[]> getAddressTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(0, 0), "A1"}
                , new Object[]{CellAddressInt.of(6, 26), "AA7"}
                , new Object[]{CellAddressInt.of(29, 27), "AB30"}
                , new Object[]{CellAddressInt.of("My_Sheet", 0, 0), "My_Sheet!A1"}
                , new Object[]{CellAddressInt.of("xx'!ab", 0, 0), "'xx''!ab'!A1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getAddressTest(CellAddressInt address, String result) {
        assertThat(address.getAddress()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> shiftByOrEmptyTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(0, 0), 0, 0, CellAddressInt.of(0, 0)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 0, 0,
                        CellAddressInt.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 3, 4,
                        CellAddressInt.of("My_Sheet", 8, 14)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -1, -2,
                        CellAddressInt.of("My_Sheet", 4, 8)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -8, 1,
                        null}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 2, -12,
                        null}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -15, -20,
                        null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByOrEmptyTest(CellAddressInt address, int rowShift, int colShift, @Nullable CellAddressInt result) {
        assertThat(address.shiftByOrEmpty(rowShift, colShift)).isEqualTo(Optional.ofNullable(result));
    }

    @Nonnull
    static Stream<Object[]> shiftByTest() {
        return Stream.of(
                new Object[]{CellAddressInt.of(0, 0), 0, 0, CellAddressInt.of(0, 0)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 0, 0,
                        CellAddressInt.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 3, 4,
                        CellAddressInt.of("My_Sheet", 8, 14)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -1, -2,
                        CellAddressInt.of("My_Sheet", 4, 8)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -8, 1,
                        null}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), 2, -12,
                        null}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10), -15, -20,
                        null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByTest(CellAddressInt address, int rowShift, int colShift, @Nullable CellAddressInt result) {
        if (result != null) {
            assertThat(address.shiftBy(rowShift, colShift)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> address.shiftBy(rowShift, colShift)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nonnull
    static Stream<Object[]> shiftBy1Test() {
        return Stream.of(
                new Object[]{CellAddressInt.of(0, 0), CellCoordinatesInt.of(0, 0),
                        CellAddressInt.of(0, 0)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10),
                        CellCoordinatesInt.of(0, 0),
                        CellAddressInt.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressInt.of("My_Sheet", 5, 10),
                        CellCoordinatesInt.of(3, 4),
                        CellAddressInt.of("My_Sheet", 8, 14)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftBy1Test(CellAddressInt address, CellCoordinates shiftBy, CellAddressInt result) {
        assertThat(address.shiftBy(shiftBy)).isEqualTo(result);
    }
}