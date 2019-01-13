package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CellReferenceImplTest {

    public static Stream<Object[]> getREGEXPTest() {
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
    public void getREGEXPTest(String address, boolean match) {
        var matcher = Pattern.compile(CellReferenceImpl.REGEXP).matcher(address);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    public static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A1", CellReferenceImpl.of(0, 0), null, null}
                , new Object[]{"AA7", CellReferenceImpl.of(6, 26), null, null}
                , new Object[]{"AB30", CellReferenceImpl.of(29, 27), null, null}
                , new Object[]{"$AB30", CellReferenceImpl.of(29, 27, false, true), null, null}
                , new Object[]{"AB$30", CellReferenceImpl.of(29, 27, true, false), null, null}
                , new Object[]{"$AB$30", CellReferenceImpl.of(29, 27, true, true), null, null}
                , new Object[]{"Sheet1!A1", CellReferenceImpl.of("Sheet1", 0, 0), null, null}
                , new Object[]{"My_Sheet!A1", CellReferenceImpl.of("My_Sheet", 0, 0, false, false), null, null}
                , new Object[]{"My_Sheet!$A1", CellReferenceImpl.of("My_Sheet", 0, 0, false, true), null, null}
                , new Object[]{"My_Sheet!A$1", CellReferenceImpl.of("My_Sheet", 0, 0, true, false), null, null}
                , new Object[]{"My_Sheet!$A$1", CellReferenceImpl.of("My_Sheet", 0, 0, true, true), null, null}
                , new Object[]{"'xx''!ab'!A1", CellReferenceImpl.of("xx'!ab", 0, 0), null, null}
                , new Object[]{null, null, NullPointerException.class, "\"A0\""}
                , new Object[]{"", null, IllegalArgumentException.class, "Empty"}
                , new Object[]{"A0", null, IllegalArgumentException.class, "\"A0\""}
                , new Object[]{"0", null, IllegalArgumentException.class, "\"0\""}
                , new Object[]{"A", null, IllegalArgumentException.class, "\"A\""}
                , new Object[]{"11", null, IllegalArgumentException.class, "\"11\""}
        );
    }

    @Test
    void parseTest(String reference, CellReferenceImpl result, Class<Throwable>  exception, String message) {
        if (exception == null) {
            assertThat(CellReferenceImpl.parse(reference)).isEqualTo(result);
        } else {
            var ex = assertThatThrownBy(() -> CellAddressImpl.parse(reference)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    @Test
    void of() {
    }

    @Test
    void isRowAbsolute() {
    }

    @Test
    void isColAbsolute() {
    }

    @Test
    void shiftByOrEmpty() {
    }

    @Test
    void shiftBy() {
    }

    @Test
    void shiftBy1() {
    }

    @Test
    void equals() {
    }

    @Test
    void hashCode() {
    }

    @Test
    void toString() {
    }
}