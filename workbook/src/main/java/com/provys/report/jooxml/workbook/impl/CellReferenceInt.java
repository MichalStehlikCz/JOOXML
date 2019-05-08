package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;
import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CellReferenceInt extends CellAddressInt implements CellReference {

    /**
     * Matches optional sheet reference (anything followed by !), followed by a run of one or more letters followed by
     * a run of one or more digits.
     * Sheet reference is optional, column and row references are mandatory.
     */
    @Nonnull
    static final String REGEX = "(?:(?:" + SheetNameFormatter.REGEXP + ')'
            + SheetNameFormatter.SHEET_NAME_DELIMITER + ")?" + "[$]?" + ColumnFormatter.REGEX + "[$]?"
            + RowFormatter.REGEX;

    /**
     * Sheet reference is group 1, run of letters is group 2 and the run of digits is group 3.
     */
    @Nonnull
    private static final String PARSE_REGEX = "(?:(" + SheetNameFormatter.REGEXP + ")" +
            SheetNameFormatter.SHEET_NAME_DELIMITER + ")?([$])?(" + ColumnFormatter.REGEX + ")([$])?(" +
            RowFormatter.REGEX + ')';
    @Nonnull
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_REGEX);

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
    @Nonnull
    static CellReferenceInt parse(String reference) {
        if (reference.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed to cell reference parsing");
        }
        Matcher matcher = PARSE_PATTERN.matcher(reference);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cell reference \"" + reference + "\"");
        }
        try {
            return of(SheetNameFormatter.parse(matcher.group(1), false).orElse(null),
                    CellCoordinatesInt.of(RowFormatter.parse(matcher.group(5)),
                            ColumnFormatter.parse(matcher.group(3))), matcher.group(4) != null,
                    matcher.group(2) != null);
        } catch (IllegalArgumentException ex) {
            // we want to see supplied string in error message
            throw new IllegalArgumentException(ex.getMessage() + "(address \"" + reference + "\")", ex);
        }
    }

    @Nonnull
    static CellReferenceInt of(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute,
                               boolean colAbsolute) {
        return new CellReferenceInt(sheetName, coordinates, rowAbsolute, colAbsolute);
    }

    @Nonnull
    static CellReferenceInt of(@Nullable String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute)
    {
        return of(sheetName, CellCoordinatesInt.of(row, col), rowAbsolute, colAbsolute);
    }

    @Nonnull
    static CellReferenceInt of(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute) {
        return of(null, coordinates, rowAbsolute, colAbsolute);
    }

    @Nonnull
    static CellReferenceInt of(int row, int col, boolean rowAbsolute, boolean colAbsolute) {
        return of(CellCoordinatesInt.of(row, col), rowAbsolute, colAbsolute);
    }

    @Nonnull
    static CellReferenceInt of(@Nullable String sheetName, CellCoordinates coordinates) {
        return of(sheetName, coordinates, false, false);
    }

    @Nonnull
    static CellReferenceInt of(@Nullable String sheetName, int row, int col) {
        return of(sheetName, CellCoordinatesInt.of(row, col));
    }

    @Nonnull
    static CellReferenceInt of(CellCoordinates coordinates) {
        return of(null, coordinates, false, false);
    }

    @Nonnull
    static CellReferenceInt of(int row, int col) {
        return of(CellCoordinatesInt.of(row, col), false, false);
    }

    private CellReferenceInt(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute,
                             boolean colAbsolute) {
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
    @Nonnull
    public Optional<? extends CellReferenceInt> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    @Nonnull
    public CellReferenceInt shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return new CellReferenceInt(getSheetName().orElse(null),
                CellCoordinatesInt.of(getRow() + rowShift, getCol() + colShift),
                isRowAbsolute(), isColAbsolute());
    }

    @Override
    @Nonnull
    public CellReferenceInt shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    @Nonnull
    public CellReference shiftBy(ContextCoordinates shift) {
        if (((this.getSheetName().isEmpty())
                || (this.getSheetName().orElseThrow(/* should be covered by isEmpty */).
                equals(shift.getSheet().getSheetName())))
                && (shift.getRowIndex() == 0) && (shift.getColumnIndex() == 0)) {
            // no move -> we can return this
            return this;
        }
        return of(this.getSheetName().isPresent() ? shift.getSheet().getSheetName() : null,
                getRow() + shift.getRowIndex(), getCol() + shift.getColumnIndex(),
                isRowAbsolute(), isColAbsolute());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CellReferenceInt that = (CellReferenceInt) o;
        return isRowAbsolute() == that.isRowAbsolute() &&
                isColAbsolute() == that.isColAbsolute();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isRowAbsolute(), isColAbsolute());
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellReferenceImpl{" +
                "sheetName=" + getSheetName().map(value -> '\'' + value + '\'').orElse("null") +
                ", row=" + getRow() +
                ", col=" + getCol() +
                ", rowAbsolute=" + isRowAbsolute() +
                ", colAbsolute=" + isColAbsolute() +
                '}';
    }
}
