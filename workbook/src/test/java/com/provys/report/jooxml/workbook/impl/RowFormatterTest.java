package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RowFormatterTest {
    public static Stream<Object[]> getREGEXPTest() {
        return Stream.of(
                new Object[]{"1", true}
                , new Object[]{"15", true}
                , new Object[]{"15432", true}
                , new Object[]{"154327", true}
                , new Object[]{"1543278", false}
                , new Object[]{"", false}
                , new Object[]{"01", false}
                , new Object[]{"-1", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getREGEXPTest(String rowString, boolean match) {
        Matcher matcher = Pattern.compile(RowFormatter.REGEXP).matcher(rowString);
        assertThat(matcher.matches()).isEqualTo(match);
        if (match) {
            assertThat(matcher.groupCount()).isEqualTo(0);
        }
    }

    public static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"1", 0, null, null}
                , new Object[]{"11", 10, null, null}
                , new Object[]{"15346", 15345, null, null}
                , new Object[]{null, -1, NullPointerException.class, null}
                , new Object[]{"", -1, IllegalArgumentException.class, "Empty"}
                , new Object[]{"0", -1, IllegalArgumentException.class, "negative"}
                , new Object[]{"-1", -1, IllegalArgumentException.class, "negative"}
                , new Object[]{"X", -1, IllegalArgumentException.class, "\"X\""}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void parseTest(String colRef, int result, Class<Throwable> exception, String message) {
        if (exception == null) {
            assertThat(RowFormatter.parse(colRef)).isEqualTo(result);
        } else {
            var ex = assertThatThrownBy(() -> RowFormatter.parse(colRef)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    public static Stream<Object[]> appendTest() {
        return Stream.of(
                new Object[]{0, "1", null}
                , new Object[]{10, "11", null}
                , new Object[]{15642, "15643", null}
                , new Object[]{-2, null, IllegalArgumentException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void appendTest(int col, String result, Class<Throwable> exception) {
        StringBuilder builder = new StringBuilder("Prefix");
        if (exception == null) {
            RowFormatter.append(builder, col);
            assertThat(builder.toString()).isEqualTo("Prefix" + result);
        } else {
            assertThatThrownBy(() -> RowFormatter.append(builder, col)).isInstanceOf(exception);
        }
    }

}