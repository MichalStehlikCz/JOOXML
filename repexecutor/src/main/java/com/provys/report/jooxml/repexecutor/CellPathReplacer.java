package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReferenceFactory;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import com.provys.report.jooxml.workbook.CellValueFactory;

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
    private static final char CELL_PREFIX_CHAR = '#';
    private static final String CELL_PREFIX = Character.toString(CELL_PREFIX_CHAR) + CELL_PREFIX_CHAR;
    private static final char CELL_POSTFIX_CHAR = '#';
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
    private static final Pattern CELL_PATH_RECORD_PATTERN = Pattern.compile("([-]?[0-9]+)" + PART_DELIMITER + ".*");
    private static final Pattern CELL_PATH_REGION_PATTERN = Pattern.compile("([A-Z][A-Z0-9_$#]*)" + PART_DELIMITER +
            ".*");

    @Nonnull
    private final CellReferenceFactory cellReferenceFactory;
    @Nonnull
    private final CellValueFactory cellValueFactory;
    @Nonnull
    // canot b static as it depends on cellReferenceFactory, that is injected to instance...
    private final Pattern cellPathCellPattern;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    CellPathReplacer(CellReferenceFactory cellReferenceFactory, CellValueFactory cellValueFactory) {
        this.cellReferenceFactory = Objects.requireNonNull(cellReferenceFactory);
        this.cellValueFactory = Objects.requireNonNull(cellValueFactory);
        this.cellPathCellPattern = Pattern.compile("(" + cellReferenceFactory.getRegex() + ")" + CELL_POSTFIX + ".*");
    }

    private void encodeCellPath(StringBuilder builder, CellPath cellPath) {
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
                encodeCellPath(builder, cellPathRecord.getChildPath());
                builder.append(RECORD_POSTFIX);
                break;
            case "CellPathRegion":
                var cellPathRegion = (CellPathRegion) cellPath;
                builder.append(REGION_PREFIX).
                        append(cellPathRegion.getRegionNm()).
                        append(PART_DELIMITER);
                encodeCellPath(builder, cellPathRegion.getChildPath());
                builder.append(REGION_POSTFIX);
                break;
            default:
                throw new RuntimeException("CellPath subtype not supported by serialization: " +
                        cellPath.getClass().getSimpleName());
        }
    }

    String encodePath(CellPath cellPath) {
        var builder = new StringBuilder("{{");
        encodeCellPath(builder, cellPath);
        builder.append("}}");
        return builder.toString();
    }

    /**
     * Replace cell references in formula text with serialized CellPath expressions
     *
     * @param formula is expression present in formula cell, potentially with original cell references
     * @param referenceMap is list of references and their corresponding paths, present in value
     * @param execRegionContext is region current cell belongs to
     * @return formula with serialized CellPath expressions
     */
    @Nullable
    private String encode(@Nullable String formula, Map<String, AreaCellPath> referenceMap,
                         ExecRegionContext execRegionContext) {
        if ((formula == null) || referenceMap.isEmpty()) {
            return formula;
        }
        var result = formula;
        for (var reference : referenceMap.entrySet()) {
            var cellPath = reference.getValue().getCellPath(execRegionContext);
            if (cellPath.isEmpty()) {
                // invalid reference - we prefer to remove formula rather than leave invalid formula
                return null;
            } else {
                result = result.replaceAll(reference.getKey(), encodePath(cellPath.get()));
            }
        }
        return result;
    }

    /**
     * Replace cell references in formula text with serialized CellPath expressions; variant working with CellValue
     *
     * @param value is cell value, potentially formula with original cell references
     * @param referenceMap is list of references and their corresponding paths, present in value
     * @param execRegionContext is region current cell belongs to
     * @return cell value with serialized CellPath expressions
     */
    @Nonnull
    public CellValue encode(CellValue value, Map<String, AreaCellPath> referenceMap,
                         ExecRegionContext execRegionContext) {
        if (value.getCellType() != CellType.FORMULA) {
            return value;
        }
        var origFormula = value.getFormula();
        var formula = encode(origFormula, referenceMap, execRegionContext);
        if (origFormula.equals(formula)) {
            return value;
        }
        return (formula == null) ? cellValueFactory.getBlank() : cellValueFactory.ofFormula(formula);
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
            if (expression.length() < offset+2) {
                throw new RuntimeException("Cannot read path - end of string while {{ expected " + this);
            }
            if (expression.charAt(offset++) != '{') {
                throw new RuntimeException("Cannot read path - opening { expected, " + expression.charAt(--offset) +
                        " found  " + this);
            }
            if (expression.charAt(offset++) != '{') {
                throw new RuntimeException("Cannot read path - second opening { expected, " +
                        expression.charAt(--offset) + " found  " + this);
            }
        }

        @Nonnull
        private CellPathCell readCellPathCell() {
            var matcher = cellPathCellPattern.matcher(expression.substring(offset));
            if (!matcher.matches()) {
                throw new RuntimeException("Failed to parse CellPathCell - pattern not matched " + this);
            }
            offset += matcher.group(1).length() + CELL_POSTFIX.length();
            return new CellPathCell(cellReferenceFactory.parse(matcher.group(1)));
        }

        @Nonnull
        private CellPathRecord readCellPathRecord() {
            var matcher = CELL_PATH_RECORD_PATTERN.matcher(expression.substring(offset));
            if (!matcher.matches()) {
                throw new RuntimeException("Failed to parse CellPathRecord - pattern not matched " + this);
            }
            offset += matcher.group(1).length() + PART_DELIMITER.length();
            var childPath = readCellPath();
            if (expression.charAt(offset++) != RECORD_POSTFIX_CHAR) {
                throw new RuntimeException("Record postfix chracter " + RECORD_POSTFIX_CHAR + " expected, " +
                        expression.charAt(--offset) + " found " + this);
            }
            if (expression.charAt(offset++) != RECORD_POSTFIX_CHAR) {
                throw new RuntimeException("Second record postfix chracter " + RECORD_POSTFIX_CHAR + " expected, " +
                        expression.charAt(--offset) + " found " + this);
            }
            return new CellPathRecord(childPath, Integer.valueOf(matcher.group(1)));
        }

        @Nonnull
        private CellPathRegion readCellPathRegion() {
            var matcher = CELL_PATH_REGION_PATTERN.matcher(expression.substring(offset));
            if (!matcher.matches()) {
                throw new RuntimeException("Failed to parse CellPathRegion - pattern not matched " + this);
            }
            offset += matcher.group(1).length() + PART_DELIMITER.length();
            var childPath = readCellPath();
            if (expression.charAt(offset++) != REGION_POSTFIX_CHAR) {
                throw new RuntimeException("Region postfix chracter " + REGION_POSTFIX_CHAR + " expected, " +
                        expression.charAt(--offset) + " found " + this);
            }
            if (expression.charAt(offset++) != REGION_POSTFIX_CHAR) {
                throw new RuntimeException("Second region postfix chracter " + REGION_POSTFIX_CHAR + " expected, " +
                        expression.charAt(--offset) + " found " + this);
            }
            return new CellPathRegion(childPath, matcher.group(1));
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
            if (expression.length() != offset+2) {
                throw new RuntimeException("Cannot read path - }} expected to close path expression " + this);
            }
            if (expression.charAt(offset++) != '}') {
                throw new RuntimeException("Cannot read path - closing } expected, " + expression.charAt(--offset) +
                        " found  " + this);
            }
            if (expression.charAt(offset++) != '}') {
                throw new RuntimeException("Cannot read path - second closing } expected, " +
                        expression.charAt(--offset) + " found  " + this);
            }
        }

        @Nonnull
        CellPath run() {
            offset = 0;
            readStartTag();
            var cellPath = readCellPath();
            readEndTag();
            return cellPath;
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
