package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellReferenceIntTest {

    static Stream<Object[]> getREGEXPTest() {
        return Stream.of(
                new Object[]{"A1", true}
                , new Object[]{"AA7", true}
                , new Object[]{"AB30", true}
                , new Object[]{"$AB30", true}
                , new Object[]{"AB$30", true}
                , new Object[]{"$AB$30", true}
                , new Object[]{"Sheet1!A1", true}
                , new Object[]{"My_Sheet!A1", true}
                , new Object[]{"My_Sheet!$A1", true}
                , new Object[]{"My_Sheet!A$1", true}
                , new Object[]{"My_Sheet!$A$1", true}
                , new Object[]{"'xx''!ab'!A1", true}
                , new Object[]{"A0", false}
                , new Object[]{"0", false}
                , new Object[]{"A", false}
                , new Object[]{"11", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getREGEXPTest(String address, boolean match) {
        var matcher = Pattern.compile(CellReferenceInt.REGEXP).matcher(address);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", CellReferenceInt.of(0, 0), null, null}
                , new Object[]{"AA7", CellReferenceInt.of(6, 26), null, null}
                , new Object[]{"AB30", CellReferenceInt.of(29, 27), null, null}
                , new Object[]{"$AB30", CellReferenceInt.of(29, 27, false, true), null, null}
                , new Object[]{"AB$30", CellReferenceInt.of(29, 27, true, false), null, null}
                , new Object[]{"$AB$30", CellReferenceInt.of(29, 27, true, true), null, null}
                , new Object[]{"Sheet1!A1", CellReferenceInt.of("Sheet1", 0, 0), null, null}
                , new Object[]{"My_Sheet!A1", CellReferenceInt.of("My_Sheet", 0, 0, false, false), null, null}
                , new Object[]{"My_Sheet!$A1", CellReferenceInt.of("My_Sheet", 0, 0, false, true), null, null}
                , new Object[]{"My_Sheet!A$1", CellReferenceInt.of("My_Sheet", 0, 0, true, false), null, null}
                , new Object[]{"My_Sheet!$A$1", CellReferenceInt.of("My_Sheet", 0, 0, true, true), null, null}
                , new Object[]{"'xx''!ab'!A1", CellReferenceInt.of("xx'!ab", 0, 0), null, null}
                , new Object[]{null, null, NullPointerException.class, null}
                , new Object[]{"", null, IllegalArgumentException.class, "Empty"}
                , new Object[]{"A0", null, IllegalArgumentException.class, "\"A0\""}
                , new Object[]{"0", null, IllegalArgumentException.class, "\"0\""}
                , new Object[]{"A", null, IllegalArgumentException.class, "\"A\""}
                , new Object[]{"11", null, IllegalArgumentException.class, "\"11\""}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String reference, CellReferenceInt result, Class<Throwable>  exception, String message) {
        if (exception == null) {
            assertThat(CellReferenceInt.parse(reference)).isEqualTo(result);
        } else {
            var ex = assertThatThrownBy(() -> CellReferenceInt.parse(reference)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    static Stream<Object[]> propertiesTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)), null,
                        CellCoordinatesInt.of(0, 0), 0, 0, false, false}
                , new Object[]{CellReferenceInt.of("Sheet1", CellCoordinatesInt.of(5, 15)),
                        "Sheet1", CellCoordinatesInt.of(5, 15), 5, 15, false, false}
                , new Object[]{CellReferenceInt.of("Sheet 1", 15674, 123), "Sheet 1",
                        CellCoordinatesInt.of(15674, 123), 15674, 123, false, false}
                , new Object[]{CellReferenceInt.of("My'sheet", 74, 5, true,
                        true), "My'sheet", CellCoordinatesInt.of(74, 5), 74, 5, true, true}
                , new Object[]{CellReferenceInt.of(74, 5, false, true), null,
                        CellCoordinatesInt.of(74, 5), 74, 5, false, true}
                , new Object[]{CellReferenceInt.of("My.Sheet", CellCoordinatesInt.of(74, 5),
                        true, false), "My.Sheet", CellCoordinatesInt.of(74, 5),
                        74, 5, true, false}
                , new Object[]{CellReferenceInt.of("A1", CellCoordinatesInt.of(12845, 15),
                        false, false), "A1", CellCoordinatesInt.of(12845, 15),
                        12845, 15, false, false}
        );
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void getSheetNameTest(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row, int col,
                          boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.getSheetName()).isEqualTo(Optional.ofNullable(sheetName));
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void getCoordinatesTest(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row,
                            int col, boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.getCoordinates()).isEqualTo(coordinates);
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void getRowTest(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row, int col,
                    boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.getRow()).isEqualTo(row);
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void getColTest(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row, int col,
                    boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.getCol()).isEqualTo(col);
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void isRowAbsolute(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row, int col,
                       boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.isRowAbsolute()).isEqualTo(rowAbsolute);
    }

    @ParameterizedTest
    @MethodSource("propertiesTest")
    void isColAbsolute(CellReferenceInt reference, String sheetName, CellCoordinates coordinates, int row, int col,
                       boolean rowAbsolute, boolean colAbsolute) {
        assertThat(reference.isColAbsolute()).isEqualTo(colAbsolute);
    }

    static Stream<Object[]> appendAddressTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)), "A1"}
                , new Object[]{CellReferenceInt.of("Sheet1", CellCoordinatesInt.of(15, 5)),
                        "Sheet1!F16"}
                , new Object[]{CellReferenceInt.of("Sheet 1", 15674, 123), "'Sheet 1'!DT15675"}
                , new Object[]{CellReferenceInt.of("My'sheet", 74, 5, true,
                        true), "'My''sheet'!$F$75"}
                , new Object[]{CellReferenceInt.of(74, 5, false, true), "$F75"}
                , new Object[]{CellReferenceInt.of("My.Sheet", CellCoordinatesInt.of(74, 5),
                        true, false), "My.Sheet!F$75"}
                , new Object[]{CellReferenceInt.of("A1", CellCoordinatesInt.of(12845, 15),
                        false, false), "'A1'!P12846"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void appendAddressTest(CellReferenceInt reference, String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        reference.appendAddress(builder);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }

    static Stream<Object[]> shiftByTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(0, 0), 0, 0, CellReferenceInt.of(0, 0)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10), 0, 0,
                        CellReferenceInt.of("My_Sheet", 5, 10)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10, true,
                        false), 3, 4, CellReferenceInt.of("My_Sheet", 8, 14,
                        true, false)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10, false,
                        true), -1, -2, CellReferenceInt.of("My_Sheet", 4, 8,
                        false, true)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10), -8, 1,
                        null}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10), 2, -12,
                        null}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10), -15, -20,
                        null}
        );
    }

    @ParameterizedTest
    @MethodSource("shiftByTest")
    void shiftByOrEmpty(CellReferenceInt reference, int rowShift, int colShift, CellReferenceInt result) {
        assertThat(reference.shiftByOrEmpty(rowShift, colShift)).isEqualTo(Optional.ofNullable(result));
    }

    @ParameterizedTest
    @MethodSource("shiftByTest")
    void shiftBy(CellReferenceInt reference, int rowShift, int colShift, CellReferenceInt result) {
        if (result != null) {
            assertThat(reference.shiftBy(rowShift, colShift)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> reference.shiftBy(rowShift, colShift)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    static Stream<Object[]> shiftBy1Test() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(0, 0), CellCoordinatesInt.of(0, 0),
                        CellReferenceInt.of(0, 0)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10),
                        CellCoordinatesInt.of(0, 0),
                        CellReferenceInt.of("My_Sheet", 5, 10)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10, true,
                        false), CellCoordinatesInt.of(3, 4),
                        CellReferenceInt.of("My_Sheet", 8, 14, true, false)}
                , new Object[]{CellReferenceInt.of("My_Sheet", 5, 10, false,
                        true), CellCoordinatesInt.of(1, 2),
                        CellReferenceInt.of("My_Sheet", 6, 12,
                        false, true)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void shiftBy1Test(CellReferenceInt reference, CellCoordinates shiftBy, CellReferenceInt result) {
        assertThat(reference.shiftBy(shiftBy)).isEqualTo(result);
    }

    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)), null, false}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)), "A1", false}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0), true}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of("Sheet", 0, 0), false}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0, true, false), false}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0, false, true), false}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0, false, false), true}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), true}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet2", 5, 15, true,
                        true), false}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 6, 15, true,
                        true), false}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 5, 10, true,
                        true), false}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 5, 15, false,
                        true), false}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 5, 15, true,
                        false), false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(CellReferenceInt reference, Object other, boolean result) {
        assertThat(reference.equals(other)).isEqualTo(result);
    }

    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0)}
                , new Object[]{CellReferenceInt.of(CellCoordinatesInt.of(0, 0)),
                        CellReferenceInt.of(0, 0, false, false)}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15, true,
                        true), CellReferenceInt.of("Sheet1", 5, 15, true,
                        true)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(CellReferenceInt reference, CellReferenceInt other) {
        if (reference.equals(other)) {
            assertThat(reference.hashCode()).isEqualTo(other.hashCode());
        }
    }

    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{CellReferenceInt.of(0, 0),
                        "CellReferenceImpl{sheetName=null, row=0, col=0, rowAbsolute=false, colAbsolute=false}"}
                , new Object[]{CellReferenceInt.of("Sheet1", 5, 15),
                        "CellReferenceImpl{sheetName=\"Sheet1\", row=5, col=15, rowAbsolute=false, colAbsolute=false}"}
                , new Object[]{CellReferenceInt.of("Sheet 1", 15674, 123),
                        "CellReferenceImpl{sheetName=\"Sheet 1\", row=15674, col=123, rowAbsolute=false, colAbsolute=false}"}
                , new Object[]{CellReferenceInt.of("My'sheet", 74, 5, true,
                        true),
                        "CellReferenceImpl{sheetName=\"My'sheet\", row=74, col=5, rowAbsolute=true, colAbsolute=true}"}
                , new Object[]{CellReferenceInt.of(74, 5, false, true),
                        "CellReferenceImpl{sheetName=null, row=74, col=5, rowAbsolute=false, colAbsolute=true}"}
                , new Object[]{CellReferenceInt.of("My.Sheet", CellCoordinatesInt.of(74, 5),
                        true, false),
                        "CellReferenceImpl{sheetName=\"My.Sheet\", row=74, col=5, rowAbsolute=true, colAbsolute=false}"}
                , new Object[]{CellReferenceInt.of("A1", 12845, 15,false, false),
                        "CellReferenceImpl{sheetName=\"A1\", row=12845, col=15, rowAbsolute=false, colAbsolute=false}"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(CellReferenceInt reference, String result) {
        assertThat(reference.toString()).isEqualTo(result);
    }
}