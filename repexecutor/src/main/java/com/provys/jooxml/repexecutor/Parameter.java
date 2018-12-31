package com.provys.jooxml.repexecutor;

/**
 * Represents parameter, passed to report execution
 */
public class Parameter {

    private final String name;
    private final String value;

    Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * @return parameter value
     */
    public String getValue() {
        return value;
    }
}
