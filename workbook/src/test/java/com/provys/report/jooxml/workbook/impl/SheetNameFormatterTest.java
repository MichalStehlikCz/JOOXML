package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class SheetNameFormatterTest {

    public static Stream<Object[]> getRegExpTest() {
        return Stream.of(
                new Object[]{"", true, ""}
                , new Object[]{"Sheet!", true, "Sheet"}
                , new Object[]{"'0123456'!", true, "'0123456'"}
                , new Object[]{"'Sheet'!", true, "'Sheet'"}
                , new Object[]{"'Test with space'!", true}
                , new Object[]{"ZZ", true}
                , new Object[]{"AAA", true}
                , new Object[]{"ZZZ", true}
                , new Object[]{"", false}
                , new Object[]{"$A", false}
                , new Object[]{"A$", false}
                , new Object[]{"1", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getRegExpTest(String colString, boolean match) {
        Pattern pattern = Pattern.compile(SheetNameFormatter.getRegExp());
        assertThat(pattern.matcher(colString).matches()).isEqualTo(match);
    }

    @ParameterizedTest
    @MethodSource
    public void parseTest() {
    }

    @ParameterizedTest
    @MethodSource
    public void appendTest() {
    }
}