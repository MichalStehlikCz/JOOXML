package com.provys.report.jooxml.workbook.impl;

import javax.annotation.Nonnull;

public class RowFormatter {

    /**
     * String that corresponds to column definition pattern
     */
    @Nonnull
    public static final String REGEX = "[1-9][0-9]{0,5}";

    /**
     * Takes in a column reference and converts it to row index. Uses int-to-string conversion and subtracts 1 as rows
     * in excel are one-based while workbook library uses zero-based indices
     *
     * @param rowString is row reference
     * @return zero based row index
     * @throws IllegalArgumentException is supplied string cannot be converted to number or it is empty string
     */
    static int parse(String rowString) {
        if (rowString.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed as row reference");
        }
        int result;
        try {
            result = Integer.parseInt(rowString) - 1;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Cannot parse row coordinate \"" + rowString + "\"", ex);
        }
        if (result < 0) {
            throw new IllegalArgumentException("Row number cannot be negative");
        }
        return result;
    }

    /**
     * Convert row index (zero based) to excel string representing the row (e.g. one-based)
     *
     * @param builder is string builder result should be appended to
     * @param row is row index (zero based)
     * @throws IllegalArgumentException if supplied row index is negative
     */
    static void append(StringBuilder builder, int row) {
        if (row < 0) {
            throw new IllegalArgumentException("Cannot convert negative number to row string");
        }
        builder.append(row + 1);
    }

    /**
     * Utility class with only static methods
     */
    private RowFormatter() {}
}
