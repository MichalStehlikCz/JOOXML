package com.provys.report.jooxml.workbook.impl;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class SheetNameFormatterTest {

    public static Stream<Object[]> getREGEXPTest() {
        return Stream.of(
                new Object[]{"", false}
                , new Object[]{"x", true}
                , new Object[]{"Sheet", true}
                , new Object[]{"'0123456'", true}
                , new Object[]{"'Sheet'", true}
                , new Object[]{"'Test with space'", true}
                , new Object[]{"'Correct''escape'", true}
                , new Object[]{"Z.Z", true}
                , new Object[]{"a_b", true}
                , new Object[]{"'Incorrect'escape'", false}
                , new Object[]{"'Missing'end", false}
                , new Object[]{"'Missing_end", false}
                , new Object[]{"$A", false}
                , new Object[]{"A$", false}
                , new Object[]{"'a'a'", false}
                , new Object[]{"1ab", false}
                , new Object[]{"With space", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getREGEXPTest(String sheetString, boolean match) {
        Matcher matcher = Pattern.compile(SheetNameFormatter.REGEXP).matcher(sheetString);
        assertThat(matcher.matches()).isEqualTo(match);
    }

    public static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{null, null, null, null}
                , new Object[]{"x", "x", null, null}
                , new Object[]{"Sheet", "Sheet", null, null}
                , new Object[]{"'0123456'", "0123456", null, null}
                , new Object[]{"'Correct''escape'", "Correct'escape", null, null}
                , new Object[]{"'Sheet'", "Sheet", null, null}
                , new Object[]{"'Test with space'", "Test with space", null, null}
                , new Object[]{"Z.Z", "Z.Z", null, null}
                , new Object[]{"a_b", "a_b", null, null}
                , new Object[]{"'Incorrect'escape'", null, IllegalArgumentException.class,
                        "\"'Incorrect'escape'\""}
                , new Object[]{"$A", null, IllegalArgumentException.class, "\"$A\""}
                , new Object[]{"A$", null, IllegalArgumentException.class, "\"A$\""}
                , new Object[]{"1ab", null, IllegalArgumentException.class, "\"1ab\""}
                , new Object[]{"With space", null, IllegalArgumentException.class, "\"With space\""}
                , new Object[]{"", null, IllegalArgumentException.class, "empty"}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void parseTest(String sheetString, String sheetName, Class<Throwable> exception, String message) {
        if (exception == null) {
            assertThat(SheetNameFormatter.parse(sheetString)).isEqualTo(Optional.ofNullable(sheetName));
        } else {
            var ex = assertThatThrownBy(() -> SheetNameFormatter.parse(sheetString)).isInstanceOf(exception);
            if (message != null) {
                ex.hasMessageContaining(message);
            }
        }
    }

    public static Stream<Object[]> appendTest() {
        return Stream.of(
                new Object[]{"Sheet", "Sheet"}
                , new Object[]{"0123456", "'0123456'"}
                , new Object[]{"Correct'escape", "'Correct''escape'"}
                , new Object[]{"Test with space", "'Test with space'"}
                , new Object[]{"Z.Z", "Z.Z"}
                , new Object[]{"a_b", "a_b"}
                , new Object[]{"A1", "'A1'"}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void appendTest(String sheetName, String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        SheetNameFormatter.append(builder, sheetName);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }

    public static Stream<Object[]> appendAddressPartTest() {
        return Stream.of(
                new Object[]{null, ""}
                , new Object[]{"Sheet", "Sheet!"}
                , new Object[]{"0123456", "'0123456'!"}
                , new Object[]{"Correct'escape", "'Correct''escape'!"}
                , new Object[]{"Test with space", "'Test with space'!"}
                , new Object[]{"Z.Z", "Z.Z!"}
                , new Object[]{"a_b", "a_b!"}
                , new Object[]{"A1", "'A1'!"}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void appendAddressPartTest(String sheetName, @Nullable String result) {
        StringBuilder builder = new StringBuilder("Prefix");
        SheetNameFormatter.appendAddressPart(builder, sheetName);
        assertThat(builder.toString()).isEqualTo("Prefix" + result);
    }
}