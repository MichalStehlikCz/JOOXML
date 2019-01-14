package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CellReferenceImpl extends CellAddressImpl implements CellReference {

    private static final Logger LOG = LogManager.getLogger(CellReferenceImpl.class.getName());

    /**
     * Matches optional sheet reference (anything followed by !), followed by a run of one or more letters followed by
     * a run of one or more digits.
     * Sheet reference is optional, column and row references are mandatory.
     */
    public static final String REGEXP = "(?:(?:" + SheetNameFormatter.REGEXP + ')'
            + SheetNameFormatter.SHEET_NAME_DELIMITER + ")?" + "[$]?" + ColumnFormatter.REGEXP + "[$]?"
            + RowFormatter.REGEXP;
    /**
     * Sheet reference is group 1, run of letters is group 2 and the run of digits is group 3.
     */
    private static final String PARSE_REGEXP = "(?:(" + SheetNameFormatter.REGEXP + ")" +
            SheetNameFormatter.SHEET_NAME_DELIMITER + ")?([$])?(" + ColumnFormatter.REGEXP + ")([$])?(" +
            RowFormatter.REGEXP + ')';
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_REGEXP);

    private final boolean rowAbsolute;
    private final boolean colAbsolute;

    /**
     * Parse cell reference and return corresponding CellReference instance.
     *
     * @param reference is cell address compatible with Excel, with no absolute row nor cell coordinate. Sheet part is
     *               optional
     * @return CellReference corresponding to supplied address
     * @throws IllegalArgumentException if supplied string is not valid Excel cell reference
     */
    static CellReferenceImpl parse(String reference) {
        if (reference.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed to cell reference parsing");
        }
        Matcher matcher = PARSE_PATTERN.matcher(reference);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cell reference \"" + reference + "\"");
        }
        try {
            return of(SheetNameFormatter.parse(matcher.group(1), false).orElse(null),
                    Workbooks.getCellCoordinates(RowFormatter.parse(matcher.group(5)),
                            ColumnFormatter.parse(matcher.group(3))), matcher.group(4) != null,
                    matcher.group(2) != null);
        } catch (IllegalArgumentException ex) {
            // we want to see supplied string in error message
            throw new IllegalArgumentException(ex.getMessage() + "(address \"" + reference + "\")", ex);
        }
    }

    static CellReferenceImpl of(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute,
                                boolean colAbsolute) {
        return new CellReferenceImpl(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    static CellReferenceImpl of(@Nullable String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute)
    {
        return of(sheetName, CellCoordinatesImpl.of(row, col), rowAbsolute, colAbsolute);
    }

    static CellReferenceImpl of(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return of(null, coordinates, rowAbsolute, colAbsolute);
    }

    static CellReferenceImpl of(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return of(CellCoordinatesImpl.of(row, col), rowAbsolute, colAbsolute);
    }

    static CellReferenceImpl of(@Nullable String sheetName, CellCoordinates coordinates) {
        return of(sheetName, coordinates, false, false);
    }

    static CellReferenceImpl of(@Nullable String sheetName, int row, int col) {
        return of(sheetName, CellCoordinatesImpl.of(row, col));
    }

    static CellReferenceImpl of(CellCoordinates coordinates) {
        return of(null, coordinates, false, false);
    }

    static CellReferenceImpl of(int row, int col) {
        return of(CellCoordinatesImpl.of(row, col), false, false);
    }

    private CellReferenceImpl(String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        super(sheetName, coordinates);
        this.rowAbsolute = rowAbsolute;
        this.colAbsolute = colAbsolute;
    }

    @Override
    public boolean isRowAbsolute() {
        return rowAbsolute;
    }

    @Override
    public boolean isColAbsolute() {
        return colAbsolute;
    }

    @Override
    public void appendAddress(StringBuilder builder) {
        getSheetName().ifPresent(sheetName -> SheetNameFormatter.appendAddressPart(builder, sheetName));
        if (isColAbsolute()) {
            builder.append('$');
        }
        ColumnFormatter.append(builder, getCol());
        if (isRowAbsolute()) {
            builder.append('$');
        }
        RowFormatter.append(builder, getRow());
    }

    @Override
    public Optional<? extends CellReferenceImpl> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    public CellReferenceImpl shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellReferenceImpl(getSheetName().orElse(null),
                Workbooks.getCellCoordinates(getRow() + rowShift, getCol() + colShift),
                isRowAbsolute(), isColAbsolute());
    }

    @Override
    public CellReferenceImpl shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CellReferenceImpl that = (CellReferenceImpl) o;
        return isRowAbsolute() == that.isRowAbsolute() &&
                isColAbsolute() == that.isColAbsolute();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isRowAbsolute(), isColAbsolute());
    }

    @Override
    public String toString() {
        return "CellReferenceImpl{" +
                "sheetName=" + getSheetName().map(value -> '"' + value + '"').orElse("null") +
                ", row=" + getRow() +
                ", col=" + getCol() +
                ", rowAbsolute=" + isRowAbsolute() +
                ", colAbsolute=" + isColAbsolute() +
                '}';
    }
}
