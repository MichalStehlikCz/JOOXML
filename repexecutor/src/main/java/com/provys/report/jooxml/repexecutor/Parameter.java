package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents parameter, passed to report execution
 */
public class Parameter {

    @Nonnull
    private final String name;
    @Nullable
    private final String value;

    /**
     * Create parameter with given name from supplied value.
     *
     * @param name is name of parameter, mandatory
     * @param value is value of parameter
     */
    Parameter(String name, @Nullable String value) {
        this.name = Objects.requireNonNull(name);
        this.value = value;
    }

    /**
     * @return parameter name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * @return parameter value
     */
    @Nonnull
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        if (!getName().equals(parameter.getName())) return false;
        return getValue().equals(parameter.getValue());
    }

    /**
     * Hashcode is only generated from name. In general, it is not expected that two Parameter instances with same name
     * would exists and thus it is not deemed necessary for them to generate distinct hash codes.
     *
     * @return hash code generated from name
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * String representation of parameter is Parameter(NAME="value"); empty value is represented as null
     */
    @Override
    public String toString() {
        return "Parameter{" + getName() + "=" + getValue().map(value -> ('\'' + value + '\'')).orElse("null")
                + "}";
    }
}
