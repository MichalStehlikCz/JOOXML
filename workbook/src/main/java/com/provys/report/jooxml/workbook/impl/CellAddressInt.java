package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellAddressInt implements CellAddress {

    /**
     * Matches optional sheet reference (anything followed by !), followed by a run of one or more letters followed by
     * a run of one or more digits.
     * Sheet reference is optional, column and row references are mandatory.
     */
    @Nonnull
    public static final String REGEXP = "(?:(?:" + SheetNameFormatter.REGEXP + ')' + SheetNameFormatter.SHEET_NAME_DELIMITER + ")?"
            + ColumnFormatter.REGEXP + RowFormatter.REGEXP;

    /**
     * Matches cell address as used in Excel, but without absolute position modifiers ($).
     *
     * Sheet reference is group 1, run of letters is group 2 and the run of digits is group 3.
     */
    private static final @Nonnull String PARSE_REGEXP = "(?:(" + SheetNameFormatter.REGEXP + ")" +
            SheetNameFormatter.SHEET_NAME_DELIMITER + ")?(" + ColumnFormatter.REGEXP + ")(" + RowFormatter.REGEXP + ')';
    private static final @Nonnull Pattern PARSE_PATTERN = Pattern.compile(PARSE_REGEXP);

    private static final @Nonnull
    CellAddressInt A1 = new CellAddressInt(null,
            CellCoordinatesInt.of(0, 0));
    private final @Nullable String sheetName;
    private final @Nonnull CellCoordinates coordinates;


    static @Nonnull
    CellAddressInt parse(String address) {
        if (address.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed to cell reference parsing");
        }
        Matcher matcher = PARSE_PATTERN.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cell address \"" + address + "\"");
        }
        try {
            return of(SheetNameFormatter.parse(matcher.group(1), false).orElse(null),
                    CellCoordinatesInt.of(RowFormatter.parse(matcher.group(3)),
                    ColumnFormatter.parse(matcher.group(2))));
        } catch (IllegalArgumentException ex) {
            // we want to see supplied string in error message
            throw new IllegalArgumentException(ex.getMessage() + "(address \"" + address + "\")", ex);
        }
    }

    /**
     * Create cell address with optional sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param coordinates are coordinates of cell on sheet
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty
     */
    static @Nonnull
    CellAddressInt of(@Nullable String sheetName, CellCoordinates coordinates) {
        if ((sheetName == null) && (coordinates.getRow() == 0) && (coordinates.getCol() == 0)) {
            return A1;
        }
        return new CellAddressInt(sheetName, coordinates);
    }

    /**
     * Create cell address - variant without sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param coordinates are coordinates of cell on sheet
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty
     */
    static @Nonnull
    CellAddressInt of(CellCoordinates coordinates) {
        return of(null, coordinates);
    }

    /**
     * Create cell address with optional sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param sheetName is name of sheet this object addresses. Can be empty, in that case cell address is considered
     *                  relative within sheet
     * @param row is row index of cell (zero based)
     * @param col is column index of cell (zero based)
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty or creation of cell coordinates fails (e.g.
     * row or column are negative)
     */
    static @Nonnull
    CellAddressInt of(@Nullable String sheetName, int row, int col) {
        return of(sheetName, CellCoordinatesInt.of(row, col));
    }

    /**
     * Create cell address without sheet reference. Might cache instances, thus is used instead of constructor
     *
     * @param row is row index of cell (zero based)
     * @param col is column index of cell (zero based)
     * @return instance of CellAddressInt with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty or creation of cell coordinates fails (e.g.
     * row or column are negative)
     */
    static @Nonnull
    CellAddressInt of(int row, int col) {
        return of(null, CellCoordinatesInt.of(row, col));
    }

    /**
     * Address constructor; use static method of instead
     */
    CellAddressInt(@Nullable String sheetName, CellCoordinates coordinates) {
        if ((sheetName != null) && (sheetName.equals(""))) {
            throw new IllegalArgumentException("Empty sheet name sent to CellAddressInt constructor");
        }
        this.sheetName = sheetName;
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    @Override
    public @Nonnull Optional<String> getSheetName() {
        return Optional.ofNullable(sheetName);
    }

    @Override
    public @Nonnull CellCoordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public int getRow() {
        return coordinates.getRow();
    }

    @Override
    public int getCol() {
        return coordinates.getCol();
    }

    @Override
    public void appendAddress(StringBuilder builder) {
        getSheetName().ifPresent(name -> SheetNameFormatter.appendAddressPart(builder, name));
        ColumnFormatter.append(builder, getCol());
        RowFormatter.append(builder, getRow());
    }

    @Override
    public @Nonnull String getAddress() {
        StringBuilder builder = new StringBuilder();
        appendAddress(builder);
        return builder.toString();
    }

    @Override
    public @Nonnull Optional<? extends CellAddressInt> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    public @Nonnull
    CellAddressInt shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return CellAddressInt.of(sheetName,
                CellCoordinatesInt.of(getRow() + rowShift, getCol() + colShift));
    }

    @Override
    public @Nonnull
    CellAddressInt shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellAddressInt that = (CellAddressInt) o;
        return Objects.equals(getSheetName(), that.getSheetName()) &&
                Objects.equals(getCoordinates(), that.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSheetName(), getCoordinates());
    }

    @Override
    public @Nonnull String toString() {
        return "CellAddressImpl{" +
                "sheetName=" + getSheetName().map(value -> '"' + value + '"').orElse("null") +
                ", row=" + getRow() +
                ", col=" + getCol() +
                '}';
    }
}
