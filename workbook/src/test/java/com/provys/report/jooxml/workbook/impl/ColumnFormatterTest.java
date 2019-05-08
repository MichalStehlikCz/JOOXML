package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ColumnFormatterTest {

    static Stream<Object[]> getREGEXPTest() {
        return Stream.of(
                new Object[]{"A", true}
                , new Object[]{"K", true}
                , new Object[]{"Z", true}
                , new Object[]{"AA", true}
                , new Object[]{"AE", true}
                , new Object[]{"ZZ", true}
                , new Object[]{"AAA", true}
                , new Object[]{"ZZZ", true}
                , new Object[]{"AAAA", false}
                , new Object[]{"", false}
                , new Object[]{"$A", false}
                , new Object[]{"A$", false}
                , new Object[]{"1", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getREGEXPTest(String colString, boolean match) {
        Matcher matcher = Pattern.compile(ColumnFormatter.REGEX).matcher(colString);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"A", 0, null, null}
                , new Object[]{"K", 10, null, null}
                , new Object[]{"Z", 25, null, null}
                , new Object[]{"AA", 26, null, null}
                , new Object[]{"AE", 30, null, null}
                , new Object[]{"ZZ", 701, null, null}
                , new Object[]{"AAA", 702, null, null}
                , new Object[]{"ZZZ", 18277, null, null}
                , new Object[]{null, -1, NullPointerException.class, null}
                , new Object[]{"", -1, IllegalArgumentException.class, "Empty"}
                , new Object[]{"$A", -1, IllegalArgumentException.class, "\"$A\""}
                , new Object[]{"A$", -1, IllegalArgumentException.class, "\"A$\""}
                , new Object[]{"1", -1, IllegalArgumentException.class, "\"1\""}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String colRef, int result, Class<Throwable> exception, String message) {
        if (exception == null) {
            assertThat(ColumnFormatter.parse(colRef)).isEqualTo(result);
        } else {
            var ex = assertThatThrownBy(() -> ColumnFormatter.parse(colRef)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    static Stream<Object[]> appendTest() {
        return Stream.of(
                new Object[]{0, "A", null}
                , new Object[]{10, "K", null}
                , new Object[]{25, "Z", null}
                , new Object[]{26, "AA", null}
                , new Object[]{30, "AE", null}
                , new Object[]{701, "ZZ", null}
                , new Object[]{702, "AAA", null}
                , new Object[]{18277, "ZZZ", null}
                , new Object[]{-1, null, IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void appendTest(int col, String result, Class<Throwable> exception) {
        StringBuilder builder = new StringBuilder("Prefix");
        if (exception == null) {
            ColumnFormatter.append(builder, col);
            assertThat(builder.toString()).isEqualTo("Prefix" + result);
        } else {
            assertThatThrownBy(() -> ColumnFormatter.append(builder, col)).isInstanceOf(exception);
        }
    }

}