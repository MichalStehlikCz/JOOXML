package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellCoordinates;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CellAddressImplTest {

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
        var matcher = Pattern.compile(CellAddressImpl.REGEXP).matcher(address);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", Workbooks.getCellAddress(0, 0), null, null}
                , new Object[]{"AA7", Workbooks.getCellAddress(6, 26), null, null}
                , new Object[]{"AB30", Workbooks.getCellAddress(29, 27), null, null}
                , new Object[]{"My_Sheet!A1", Workbooks.getCellAddress("My_Sheet", 0, 0), null, null}
                , new Object[]{"'xx''!ab'!A1", Workbooks.getCellAddress("xx'!ab", 0, 0), null, null}
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
    void parseTest(String addressString, CellAddress address, Class<Throwable> exception, String message) {
        if (exception == null) {
            assertThat(CellAddressImpl.parse(addressString)).isEqualTo(address);
        } else {
            var ex = assertThatThrownBy(() -> CellAddressImpl.parse(addressString)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{null, Workbooks.getCellCoordinates(0, 0), null}
                , new Object[]{"Sheet1", Workbooks.getCellCoordinates(5, 15), null}
                , new Object[]{"Sheet 1", Workbooks.getCellCoordinates(5, 15), null}
                , new Object[]{"", Workbooks.getCellCoordinates(0, 0), IllegalArgumentException.class}
                , new Object[]{null, null, NullPointerException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(String sheetName, CellCoordinates coordinates, Class<Throwable> exception) {
        if (exception == null) {
            //noinspection DuplicateExpressions
            assertThatCode(() -> CellAddressImpl.of(sheetName, coordinates)).doesNotThrowAnyException();
        } else {
            //noinspection DuplicateExpressions
            assertThatThrownBy(() -> CellAddressImpl.of(sheetName, coordinates)).isInstanceOf(exception);
        }
    }

    static Stream<Object[]> getSheetNameTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(null, Workbooks.getCellCoordinates(0, 0)), null}
                , new Object[]{CellAddressImpl.of("Sheet1", Workbooks.getCellCoordinates(5, 15)),
                        "Sheet1"}
                , new Object[]{CellAddressImpl.of("Sheet 1", Workbooks.getCellCoordinates(123, 15674)),
                        "Sheet 1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getSheetNameTest(CellAddressImpl address, String sheetName) {
        assertThat(address.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
    }

    static Stream<Object[]> getCoordinatesTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(null, Workbooks.getCellCoordinates(0, 0)),
                        Workbooks.getCellCoordinates(0, 0)}
                , new Object[]{CellAddressImpl.of("Sheet1", Workbooks.getCellCoordinates(5, 15)),
                        Workbooks.getCellCoordinates(5, 15)}
                , new Object[]{CellAddressImpl.of("Sheet 1", Workbooks.getCellCoordinates(123, 15674)),
                        Workbooks.getCellCoordinates(123, 15674)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getCoordinatesTest(CellAddressImpl address, CellCoordinates coordinates) {
        assertThat(address.getCoordinates()).isEqualTo(coordinates);
    }

    static Stream<Object[]> getRowTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(null, Workbooks.getCellCoordinates(0, 0)), 0}
                , new Object[]{CellAddressImpl.of("Sheet1", Workbooks.getCellCoordinates(5, 15)),5}
                , new Object[]{CellAddressImpl.of("Sheet 1", Workbooks.getCellCoordinates(123, 15674)), 123}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getRowTest(CellAddressImpl address, int row) {
        assertThat(address.getRow()).isEqualTo(row);
    }

    static Stream<Object[]> getColTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(null, Workbooks.getCellCoordinates(0, 0)), 0}
                , new Object[]{CellAddressImpl.of("Sheet1", Workbooks.getCellCoordinates(5, 15)), 15}
                , new Object[]{CellAddressImpl.of("Sheet 1", Workbooks.getCellCoordinates(123, 15674)), 15674}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getColTest(CellAddressImpl address, int col) {
        assertThat(address.getCol()).isEqualTo(col);
    }

    static Stream<Object[]> appendAddressTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(0, 0), "A1"}
                , new Object[]{CellAddressImpl.of(6, 26), "AA7"}
                , new Object[]{CellAddressImpl.of(29, 27), "AB30"}
                , new Object[]{CellAddressImpl.of("My_Sheet", 0, 0), "My_Sheet!A1"}
                , new Object[]{CellAddressImpl.of("xx'!ab", 0, 0), "'xx''!ab'!A1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void appendAddressTest(CellAddressImpl address, String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        address.appendAddress(builder);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }

    static Stream<Object[]> getAddressTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(0, 0), "A1"}
                , new Object[]{CellAddressImpl.of(6, 26), "AA7"}
                , new Object[]{CellAddressImpl.of(29, 27), "AB30"}
                , new Object[]{CellAddressImpl.of("My_Sheet", 0, 0), "My_Sheet!A1"}
                , new Object[]{CellAddressImpl.of("xx'!ab", 0, 0), "'xx''!ab'!A1"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getAddressTest(CellAddressImpl address, String result) {
        assertThat(address.getAddress()).isEqualTo(result);
    }

    static Stream<Object[]> shiftByOrEmptyTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(0, 0), 0, 0, CellAddressImpl.of(0, 0)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 0, 0,
                        CellAddressImpl.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 3, 4,
                        CellAddressImpl.of("My_Sheet", 8, 14)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -1, -2,
                        CellAddressImpl.of("My_Sheet", 4, 8)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -8, 1,
                        null}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 2, -12,
                        null}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -15, -20,
                        null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByOrEmptyTest(CellAddressImpl address, int rowShift, int colShift, @Nullable CellAddress result) {
        assertThat(address.shiftByOrEmpty(rowShift, colShift)).isEqualTo(Optional.ofNullable(result));
    }

    static Stream<Object[]> shiftByTest() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(0, 0), 0, 0, CellAddressImpl.of(0, 0)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 0, 0,
                        CellAddressImpl.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 3, 4,
                        CellAddressImpl.of("My_Sheet", 8, 14)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -1, -2,
                        CellAddressImpl.of("My_Sheet", 4, 8)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -8, 1,
                        null}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), 2, -12,
                        null}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10), -15, -20,
                        null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByTest(CellAddressImpl address, int rowShift, int colShift, @Nullable CellAddress result) {
        if (result != null) {
            assertThat(address.shiftBy(rowShift, colShift)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> address.shiftBy(rowShift, colShift)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    static Stream<Object[]> shiftBy1Test() {
        return Stream.of(
                new Object[]{CellAddressImpl.of(0, 0), Workbooks.getCellCoordinates(0, 0),
                        CellAddressImpl.of(0, 0)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10),
                        Workbooks.getCellCoordinates(0, 0),
                        CellAddressImpl.of("My_Sheet", 5, 10)}
                , new Object[]{CellAddressImpl.of("My_Sheet", 5, 10),
                        Workbooks.getCellCoordinates(3, 4),
                        CellAddressImpl.of("My_Sheet", 8, 14)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftBy1Test(CellAddressImpl address, CellCoordinates shiftBy, CellAddressImpl result) {
        assertThat(address.shiftBy(shiftBy)).isEqualTo(result);
    }
}