package com.provys.report.jooxml.repexecutor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ParameterTest {

    static Stream<Object[]> newTest() {
        return Stream.of(
                new Object[] {"NAME", "VALUE", null}
                , new Object[] {"NAME", null, null}
                , new Object[] {null, "VALUE", NullPointerException.class});
    }

    @ParameterizedTest
    @MethodSource
    void newTest(String name, String value, Class<Throwable> exception) {
        if (exception == null) {
            assertThatCode(() -> new Parameter(name, value)).doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> new Parameter(name, value)).isInstanceOf(exception);
        }
    }

    static Stream<Object[]> getName() {
        return Stream.of(
                new Object[] {new Parameter("NAME", "value"), "NAME"}
                , new Object[] {new Parameter("name", (String) null), "name"});
    }

    @ParameterizedTest
    @MethodSource
    void getName(Parameter parameter, String name) {
        assertThat(parameter.getName()).isEqualTo(name);
    }

    static Stream<Object[]> getValue() {
        return Stream.of(
                new Object[] {new Parameter("NAME", "value"), Optional.of("value")}
                , new Object[] {new Parameter("name", (String) null), Optional.empty()});
    }

    @ParameterizedTest
    @MethodSource
    void getValue(Parameter parameter, Optional<String> value) {
        assertThat(parameter.getValue()).isEqualTo(value);
    }

    static Stream<Object[]> equals() {
        return Stream.of(
                new Object[] {new Parameter("NAME", "value"), null, false}
                , new Object[] {new Parameter("NAME", "value"), "NAME", false}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", "value")
                        , true}
                , new Object[] {new Parameter("NAME", (String) null), new Parameter("NAME", (String) null)
                        , true}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", (String) null)
                        , false}
                , new Object[] {new Parameter("NAME", (String) null), new Parameter("NAME", "")
                        , false}
                , new Object[] {new Parameter("NAME", ""), new Parameter("NAME", (String) null)
                        , false}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", "value2")
                        , false}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME2", "value")
                        , false});
    }

    @ParameterizedTest
    @MethodSource
    void equals(Parameter parameter, Object other, boolean equal) {
        assertThat(parameter.equals(other)).isEqualTo(equal);
    }

    static Stream<Object[]> hashCodeTest() {
        return Stream.of(
                new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", "value")}
                , new Object[] {new Parameter("NAME", (String) null), new Parameter("NAME", (String) null)}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", (String) null)}
                , new Object[] {new Parameter("NAME", (String) null), new Parameter("NAME", "")}
                , new Object[] {new Parameter("NAME", ""), new Parameter("NAME", (String) null)}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME", "value2")}
                , new Object[] {new Parameter("NAME", "value"), new Parameter("NAME2", "value")});
    }

    @ParameterizedTest
    @MethodSource
    void hashCodeTest(Parameter parameter, Object other) {
        if (parameter.equals(other)) {
            assertThat(parameter.hashCode()).isEqualTo(other.hashCode());
        }
    }

    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[] {new Parameter("NAME", "value"), "Parameter(NAME: \"value\")"}
                , new Object[] {new Parameter("NAME", (String) null), "Parameter(NAME: <null>)"});
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(Parameter parameter, String result) {
        assertThat(parameter.toString()).isEqualTo(result);
    }
}