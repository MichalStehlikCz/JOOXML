package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CellAddressTest {

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
}