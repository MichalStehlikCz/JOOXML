package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellCoordinatesImplTest {

    static Stream<Object[]> getRegExpTest() {
        return Stream.of(
                new Object[]{"A1", true, "A", "1"}
                , new Object[]{"AA7", true, "AA", "7"}
                , new Object[]{"AB30", true, "AB", "30"}
                , new Object[]{"A0", false, null, null}
                , new Object[]{"0", false, null, null}
                , new Object[]{"A", false, null, null}
                , new Object[]{"11", false, null, null}
                , new Object[]{"$A1", false, null, null}
                , new Object[]{"A$1", false, null, null}
                , new Object[]{"Sheet!A1", false, null, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getRegExpTest(String address, boolean match, String group1, String group2) {
        var matcher = Pattern.compile(CellCoordinatesImpl.REGEXP).matcher(address);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", CellCoordinatesImpl.of(0, 0), null, null}
                , new Object[]{"AA7", CellCoordinatesImpl.of(6, 26), null, null}
                , new Object[]{"AB30", CellCoordinatesImpl.of(29, 27), null, null}
                , new Object[]{"A0", null, IllegalArgumentException.class, "\"A0\""}
                , new Object[]{"0", null, IllegalArgumentException.class, "\"0\""}
                , new Object[]{"A", null, IllegalArgumentException.class, "\"A\""}
                , new Object[]{"11", null, IllegalArgumentException.class, "\"11\""}
                , new Object[]{"$A1", null, IllegalArgumentException.class, "\"$A1\""}
                , new Object[]{"A$1", null, IllegalArgumentException.class, "\"A$1\""}
                , new Object[]{"Sheet!A1", null, IllegalArgumentException.class, "\"Sheet!A1\""}
                , new Object[]{null, null, NullPointerException.class, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String address, CellCoordinates coordinates, Class<Throwable> exception, String message) {
        if (exception == null) {
            assertThat(CellCoordinatesImpl.parse(address)).isEqualTo(coordinates);
        } else {
            var ex = assertThatThrownBy(() -> CellCoordinatesImpl.parse(address)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    static Stream<Object[]> ofTest() {
        return Stream.of(
                new Object[]{0, 0, null}
                , new Object[]{26, 8, null}
                , new Object[]{27, 31, null}
                , new Object[]{-5, 10, IllegalArgumentException.class}
                , new Object[]{10, -5, IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofTest(int row, int col, Class<Throwable> exception) {
        if (exception == null) {
            //noinspection DuplicateExpressions
            assertThatCode(() -> CellCoordinatesImpl.of(row, col)).doesNotThrowAnyException();
        } else {
            //noinspection DuplicateExpressions
            assertThatThrownBy(() -> CellCoordinatesImpl.of(row, col)).isInstanceOf(exception);
        }
    }

    @Test
    void getRowTest() {
        assertThat(CellCoordinatesImpl.of(5, 3).getRow()).isEqualTo(5);
    }

    @Test
    void getColTest() {
        assertThat(CellCoordinatesImpl.of(5, 3).getCol()).isEqualTo(3);
    }

    static Stream<Object[]> appendAddressTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), "A1"}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), "AG15235"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void appendAddressTest(CellCoordinatesImpl coordinates, String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        coordinates.appendAddress(builder);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }

    static Stream<Object[]> getAddressTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), "A1"}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), "AG15235"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getAddressTest(CellCoordinatesImpl coordinates, String result) {
        assertThat(coordinates.getAddress()).isEqualTo(result);
    }

    static Stream<Object[]> shiftByOrEmptyTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), 0, 0, CellCoordinatesImpl.of(0, 0)}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), 3, 5,
                        CellCoordinatesImpl.of(15237, 37)}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), 3, -5,
                        CellCoordinatesImpl.of(15237, 27)}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -3, 5,
                        CellCoordinatesImpl.of(15231, 37)}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -3, -50, null}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -30000, 50, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByOrEmptyTest(CellCoordinatesImpl coordinates, int rowShift, int colShift,
                            @Nullable CellCoordinates result) {
        assertThat(coordinates.shiftByOrEmpty(rowShift, colShift)).isEqualTo(Optional.ofNullable(result));
    }

    static Stream<Object[]> shiftByTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), 0, 0,
                        CellCoordinatesImpl.of(0, 0), null}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), 3, 5,
                        CellCoordinatesImpl.of(15237, 37), null}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), 3, -5,
                        CellCoordinatesImpl.of(15237, 27), null}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -3, 5,
                        CellCoordinatesImpl.of(15231, 37), null}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -3, -50, null,
                        IllegalArgumentException.class}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), -30000, 50, null,
                        IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftByTest(CellCoordinatesImpl coordinates, int rowShift, int colShift, CellCoordinates result,
                     Class<Throwable> exception) {
        if (exception == null) {
            assertThat(coordinates.shiftBy(rowShift, colShift)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> coordinates.shiftBy(rowShift, colShift)).isInstanceOf(exception);
        }
    }

    static Stream<Object[]> shiftBy1Test() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), CellCoordinatesImpl.of(0, 0),
                        CellCoordinatesImpl.of(0, 0)}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), CellCoordinatesImpl.of(3, 5),
                        CellCoordinatesImpl.of(15237, 37)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftBy1Test(CellCoordinatesImpl coordinates, CellCoordinates shiftBy, CellCoordinates result) {
        assertThat(coordinates.shiftBy(shiftBy)).isEqualTo(result);
    }

    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), CellCoordinatesImpl.of(0, 0), true}
                , new Object[]{CellCoordinatesImpl.of(15, 20), CellCoordinatesImpl.of(15, 20), true}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), CellCoordinatesImpl.of(15237, 27),
                        false}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), null, false}
                , new Object[]{CellCoordinatesImpl.of(15234, 32), "aaa", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellCoordinatesImpl coordinates, Object other, Boolean result) {
        assertThat(coordinates.equals(other)).isEqualTo(result);
    }

    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), CellCoordinatesImpl.of(0, 0)}
                , new Object[]{CellCoordinatesImpl.of(15, 20), CellCoordinatesImpl.of(15, 20)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(CellCoordinatesImpl coordinates, CellCoordinatesImpl other) {
        if (coordinates.equals(other)) {
            assertThat(coordinates.hashCode()).isEqualTo(other.hashCode());
        }
    }

    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{CellCoordinatesImpl.of(0, 0), "CellCoordinatesImpl{row=0, col=0}"}
                , new Object[]{CellCoordinatesImpl.of(15, 15875), "CellCoordinatesImpl{row=15, col=15875}"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(CellCoordinatesImpl coordinates, String result) {
        assertThat(coordinates.toString()).isEqualTo(result);
    }
}