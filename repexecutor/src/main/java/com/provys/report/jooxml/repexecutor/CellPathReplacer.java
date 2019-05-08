package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReferenceFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Used to replace cell references with serialized CellPath expressions during report creation and serialized CellPath
 * expressions in formula text with actual cell reference after export (when data are injected to xlsx package from
 * temporary file)
 */
@ApplicationScoped
public class CellPathReplacer {

    static final Pattern ENCODED_PATH_PATTERN = Pattern.compile("(\\{\\{[^{}]*}})");
    private static final char CELL_PREFIX_CHAR = '$';
    private static final String CELL_PREFIX = Character.toString(CELL_PREFIX_CHAR) + CELL_PREFIX_CHAR;
    private static final char CELL_POSTFIX_CHAR = '$';
    private static final String CELL_POSTFIX = Character.toString(CELL_POSTFIX_CHAR) + CELL_POSTFIX_CHAR;
    private static final char RECORD_PREFIX_CHAR = '[';
    private static final String RECORD_PREFIX = Character.toString(RECORD_PREFIX_CHAR) + RECORD_PREFIX_CHAR;
    private static final char RECORD_POSTFIX_CHAR = ']';
    private static final String RECORD_POSTFIX = Character.toString(RECORD_POSTFIX_CHAR) + RECORD_POSTFIX_CHAR;
    private static final char REGION_PREFIX_CHAR = '<';
    private static final String REGION_PREFIX = Character.toString(REGION_PREFIX_CHAR) + REGION_PREFIX_CHAR;
    private static final char REGION_POSTFIX_CHAR = '<';
    private static final String REGION_POSTFIX = Character.toString(REGION_POSTFIX_CHAR) + REGION_POSTFIX_CHAR;
    private static final char PART_DELIMITER_CHAR = '/';
    private static final String PART_DELIMITER = Character.toString(PART_DELIMITER_CHAR);

    @Nonnull
    private final CellReferenceFactory cellReferenceFactory;
    @Nonnull
    private final Pattern cellPathPattern;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    CellPathReplacer(CellReferenceFactory cellReferenceFactory) {
        this.cellReferenceFactory = Objects.requireNonNull(cellReferenceFactory);
        this.cellPathPattern = Pattern.compile("(" + cellReferenceFactory.getRegex() + ")" + CELL_POSTFIX + ".*");
    }

    void encodePath(StringBuilder builder, CellPath cellPath) {
        switch (cellPath.getClass().getSimpleName()) {
            case "CellPathCell":
                builder.append(CELL_PREFIX).
                        append(((CellPathCell) cellPath).getCellReference().getAddress()).
                        append(CELL_POSTFIX);
                break;
            case "CellPathRecord":
                var cellPathRecord = (CellPathRecord) cellPath;
                builder.append(RECORD_PREFIX).
                        append(cellPathRecord.getRecordNr()).
                        append(PART_DELIMITER);
                encodePath(builder, cellPathRecord.getChildPath());
                builder.append(RECORD_POSTFIX);
                break;
            case "CellPathRegion":
                var cellPathRegion = (CellPathRegion) cellPath;
                builder.append(REGION_PREFIX).
                        append(cellPathRegion.getRegionNm()).
                        append(PART_DELIMITER);
                encodePath(builder, cellPathRegion.getChildPath());
                builder.append(REGION_POSTFIX);
                break;
            default:
                throw new RuntimeException("CellPath subtype not supported by serialization: " +
                        cellPath.getClass().getSimpleName());
        }
    }
    /**
     * Replace cell references in formula text with serialized CellPath expressions
     *
     * @param formula is expression present in formula cell, potentially with original cell references
     * @param execRegion is region current cell belongs to
     * @return formula with serialized CellPath expressions
     */
    @Nullable
    public String encode(@Nullable String formula, Map<String, AreaCellPath> referenceMap, ExecRegion execRegion) {
        if (formula == null) {
            return null;
        }
        var result = formula;
        for (var reference : referenceMap.entrySet()) {
            var cellPath = reference.getValue().getCellPath(execRegion);
            if (cellPath.isEmpty()) {
                // invalid reference - we prefer to remove formula rather than leave invalid formula
                return null;
            } else {
                var builder = new StringBuilder("{{");
                encodePath(builder, cellPath.get());
                builder.append("}}");
                result = result.replaceAll(reference.getKey(), builder.toString());
            }
        }
        return result;
    }

    /**
     * Process given string expression, representing encoded CellPath and return decoded CellPath using run function.
     */
    private class PathDecoder {

        @Nonnull
        private final String expression;
        private int offset;

        PathDecoder(String expression) {
            this.expression = Objects.requireNonNull(expression);
        }

        private void readStartTag() {
            if (expression.length() < offset+1) {
                throw new RuntimeException("Cannot read path - end of string while {{ expected " + this);
            }
            if (expression.charAt(offset) != '{') {
                throw new RuntimeException("Cannot read path - opening { expected, " + expression.charAt(offset) +
                        " found  " + this);
            }
            if (expression.charAt(++offset) != '{') {
                throw new RuntimeException("Cannot read path - second opening { expected, " + expression.charAt(offset) +
                        " found  " + this);
            }
            offset++;
        }

        @Nonnull
        private CellPathCell readCellPathCell() {
            var matcher = cellPathPattern.matcher(expression.substring(offset));
            if (!matcher.matches()) {
                throw new RuntimeException("Failed to parse CellPathCell - pattern not matched " + this);
            }
            offset += matcher.group(1).length() + 2;
            return new CellPathCell(cellReferenceFactory.parse(matcher.group(1)));
        }

        @Nonnull
        private CellPathRecord readCellPathRecord() {
            return null;
        }

        @Nonnull
        private CellPathRegion readCellPathRegion() {
            return null;
        }

        private char readOpenCellPath() {
            var prefixChar = expression.charAt(offset++);
            var nextChar = expression.charAt(offset++);
            if (prefixChar != nextChar) {
                offset -= 2;
                throw new RuntimeException("Incorrect cell path prefix - second character " + nextChar +
                        "does not match first " + prefixChar + " " + this);
            }
            return prefixChar;
        }

        private CellPath readCellPath() {
            var prefixChar = readOpenCellPath();
            switch (prefixChar) {
                case CELL_PREFIX_CHAR:
                    return readCellPathCell();
                case RECORD_PREFIX_CHAR:
                    return readCellPathRecord();
                case REGION_PREFIX_CHAR:
                    return readCellPathRegion();
                default:
                    throw new RuntimeException("Unexpected opening character of CellPath definition " +
                            expression.charAt(offset));
            }
        }

        private void readEndTag() {
            if (expression.length() != offset+1) {
                throw new RuntimeException("Cannot read path - }} expected to close path expression (\"" + expression +
                        "\", position " + offset + ")");
            }
            if (expression.charAt(offset) != '}') {
                throw new RuntimeException("Cannot read path - first closing } expected, " + expression.charAt(offset) +
                        " found  (\"" + expression + "\", position " + offset + ")");
            }
            if (expression.charAt(++offset) != '}') {
                throw new RuntimeException("Cannot read path - closing } expected, " + expression.charAt(offset) +
                        " found  (\"" + expression + "\", position " + offset + ")");
            }
        }

        @Nonnull
        CellPath run() {
            offset = 0;
            readStartTag();
            readEndTag();
            return null;
        }

        @Override
        public String toString() {
            return "PathDecoder{" +
                    "expression='" + expression + '\'' +
                    ", offset=" + offset +
                    '}';
        }
    }

    @Nonnull
    CellPath decodePath(String expression) {
        var decoder = new PathDecoder(expression);
        return decoder.run();
    }

    /**
     * Find cellpath expressions in formula text, parse them and replace them with actual cell reference
     *
     * @param formula is expression present in formula cell, potentially with embedded CellPath expressions
     * @param execRegion is region map from report execution
     * @return formula with valid cell references, corresponding to CellPath expressions
     */
    @Nullable
    @SuppressWarnings("squid:S3655") // Sonar doesn't understand Optional.isEmpty and thus throws false warnings :(
    public String decode(@Nullable String formula, ExecRegion execRegion) {
        if (formula == null) {
            return null;
        }
        var buffer = new StringBuffer();
        var matcher = ENCODED_PATH_PATTERN.matcher(formula);
        while (matcher.find()) {
            var cell = execRegion.getCell(decodePath(matcher.group(1)));
            if (cell.isEmpty()) {
                // we were not able to evaluate referenced cell -> whole formula should be removed to prevent invalid
                // formula errors
                return null;
            }
            matcher.appendReplacement(buffer, cell.get().getAddress());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
