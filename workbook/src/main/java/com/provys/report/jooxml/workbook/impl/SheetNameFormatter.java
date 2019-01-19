package com.provys.report.jooxml.workbook.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class with methods related to sheet name translation between column index and Excel character column
 * representation.
 */
public class SheetNameFormatter {

    /** The character (!) that separates sheet names from cell references */
    public static final char SHEET_NAME_DELIMITER = '!';
    /** The character (') used to quote sheet names when they contain special characters */
    private static final char SPECIAL_NAME_DELIMITER = '\'';
    /** Regexp for sheet name without escaping */
    @Nonnull
    private static final String NO_ESCAPE_REGEXP = "[a-zA-Z][0-9a-zA-Z._]*";
    /** Pattern for matching of sheet name without escaping */
    @Nonnull
    private static final Pattern NO_ESCAPE_PATTERN = Pattern.compile(NO_ESCAPE_REGEXP);
    /** Pattern for matching cell address - used to decide if escaping is to be used */
    @Nonnull
    private static final Pattern CELL_ADDRESS_PATTERN = Pattern.compile("[$]?" + ColumnFormatter.REGEXP + "[$]?" +
            RowFormatter.REGEXP);
    /**
     * String that corresponds to sheet specification pattern; it is either valid unescaped sheet name or escaped sheet
     * string
     */
    @Nonnull
    public static final String REGEXP = "(?:" + NO_ESCAPE_REGEXP + ")" +
            "|(?:" + SPECIAL_NAME_DELIMITER +
            "(?:[^" + SPECIAL_NAME_DELIMITER + "]|" + SPECIAL_NAME_DELIMITER + SPECIAL_NAME_DELIMITER + ")+" +
            SPECIAL_NAME_DELIMITER + ")";

    /**
     * Takes in a sheet reference and converts it from potentially escaped string format (excluding final !) to sheet
     * name.
     *
     * @param sheetString is character sheet reference, excluding final !
     * @param validate defines if validation of supplied string as sheet name should be performed. It an be turned off
     *                 if sheet name has been already parsed using supplied REGEXP
     * @return non-escaped sheet name
     * @throws IllegalArgumentException if supplied string is empty or does not contain valid sheet representation
     */
    @Nonnull
    static Optional<String> parse(@Nullable String sheetString, boolean validate) {
        if (sheetString == null) {
            return Optional.empty();
        }
        if (sheetString.isEmpty()) {
            throw new IllegalArgumentException("Sheet name cannot be empty");
        }
        if (sheetString.charAt(0) != SPECIAL_NAME_DELIMITER) {
            // unquoted sheet name - return as it is
            if (validate && (!NO_ESCAPE_PATTERN.matcher(sheetString).matches())) {
                throw new IllegalArgumentException("Sheet name without escaping does not match pattern: \"" +
                        sheetString + "\"");
            }
            return Optional.of(sheetString);
        }
        if (sheetString.length() < 3) {
            throw new IllegalArgumentException("\"" + sheetString + "\" is not valid sheet name enclosed in ''");
        }
        if (sheetString.charAt(sheetString.length() - 1) != '\'') {
            throw new IllegalArgumentException("Ending ' expected in escaped sheet name \"" + sheetString + "\"");
        }
        StringBuilder builder = new StringBuilder(sheetString.length()-2);
        int i = 1;
        while (i < sheetString.length()-1) { // skip outer quotes
            char ch = sheetString.charAt(i);
            if (ch == SPECIAL_NAME_DELIMITER) {
                i++;
                ch = sheetString.charAt(i);
                if ((i == sheetString.length()-1) || (ch != SPECIAL_NAME_DELIMITER)) {
                    throw new IllegalArgumentException("Non-pair ' character in escaped sheet name \"" +
                            sheetString + "\"");
                }
            }
            builder.append(ch);
            i++;
        }
        return Optional.of(builder.toString());
    }

    /**
     * Takes in a sheet reference and converts it from potentially escaped string format (excluding final !) to sheet
     * name.
     *
     * @param sheetString is character sheet reference, excluding final !
     * @return non-escaped sheet name
     * @throws IllegalArgumentException if supplied string is empty or does not contain valid sheet representation
     */
    @Nonnull
    public static Optional<String> parse(String sheetString) {
        return parse(sheetString, true);
    }

    /**
     * Checks if sheet name needs escaping
     */
    private static boolean needsEscaping(String sheetName) {
        // check if sheet name starts with letter and contains only letters, numbers, . and _
        if (!NO_ESCAPE_PATTERN.matcher(sheetName).matches()) {
            return true;
        }
        return CELL_ADDRESS_PATTERN.matcher(sheetName).matches();
    }

    /**
     * Convert sheet name to valid sheet name string - escape with ' if sheet name does not fulfill conditions for
     * simple sheet name
     *
     * @param builder is string builder result should be appended to
     * @param sheetName is sheet name to be appended; might be null but cannot be empty
     * @throws IllegalArgumentException if supplied sheet name is empty string
     */
    static void append(StringBuilder builder, String sheetName) {
        if (sheetName.isEmpty()) {
            throw new IllegalArgumentException("Sheet name cannot be empty");
        }
        if (needsEscaping(sheetName)) {
            builder.append(SPECIAL_NAME_DELIMITER);
            // escape special name delimiter inside sheet name
            for (int i = 0; i < sheetName.length(); i++) {
                char ch = sheetName.charAt(i);
                builder.append(ch);
                if (ch == SPECIAL_NAME_DELIMITER) {
                    builder.append(ch);
                }
            }
            builder.append(SPECIAL_NAME_DELIMITER);
        } else {
            builder.append(sheetName);
        }
    }

    /**
     * Append part of address representing sheet name. Similar to append, but adds ! after the end of sheet name and
     * supports null sheetName parameter, in that case, appends nothing
     *
     * @param builder is string builder sheet name should be appended to
     * @param sheetName is sheet name, potentially null
     */
    static void appendAddressPart(StringBuilder builder, @Nullable String sheetName) {
        if (sheetName != null) {
            append(builder, sheetName);
            builder.append(SHEET_NAME_DELIMITER);
        }
    }

    /**
     * Utility class with only static methods
     */
    private SheetNameFormatter() {}
}
