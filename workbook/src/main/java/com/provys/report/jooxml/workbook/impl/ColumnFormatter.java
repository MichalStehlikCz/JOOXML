package com.provys.report.jooxml.workbook.impl;

import java.util.Locale;

/**
 * Utility class with methods related to column address translation between column index and Excel character column
 * representation (e.g. A, B, AA, ...)
 */
public class ColumnFormatter {

    /**
     * String that corresponds to column definition pattern
     */
    private static final String REGEXP = "([a-zA-Z]+)";

    /**
     * Get String that will match valid column specification
     */
    public static String getRegExp() {
        return REGEXP;
    }

    /**
     * Takes in a column reference and converts it from ALPHA-26 number format to 0-based column index.
     * 'A' -&gt; 0
     * 'Z' -&gt; 25
     * 'AA' -&gt; 26
     * 'IV' -&gt; 255
     *
     * @param colString is character column reference
     * @return zero based column index
     * @throws IllegalArgumentException is supplied string contains other characters than letters or it is empty string
     */
    static int parse(String colString) {
        if (colString.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed as column reference");
        }
        int col = 0;
        char[] colRefArray = colString.toUpperCase(Locale.ROOT).toCharArray();
        for (char colRef : colRefArray) {
            if ((colRef < 'A') || (colRef > 'Z')) {
                throw new IllegalArgumentException("Character other than upper-case letter found in column reference \""
                        + colString + "\"");
            }
            col = (26 * col) + (colRef - 'A' + 1); // 'A' means 1, ...
        }
        return col-1;
    }

    /**
     * Convert column index (zero based) to excel string representing the column (e.g. A, B, ...)
     *
     * @param builder is string builder result should be appended to
     * @param col is column index (zero based)
     * @throws IllegalArgumentException if supplied column index is negative
     */
    static void append(StringBuilder builder, int col) {
        if (col < 0) {
            throw new IllegalArgumentException("Cannot convert negative number to column string");
        }
        if (col > 25) {
            append(builder, (col / 26) - 1);
        }
        builder.append((char) ((col % 26) + 65)); // 64 is 1 for zero > one based + 64 code of A
    }

    /**
     * Utility class with only static methods
     */
    private ColumnFormatter() {};
}
