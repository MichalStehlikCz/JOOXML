package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellCoordinatesInt implements CellCoordinates {

    private static final Logger LOG = LogManager.getLogger(CellCoordinatesInt.class.getName());

    /**
     * Regular expression for matching coordinates - one or more letters representing column coordinate
     * plus one or more numbers representing row coordinate
     */
    @Nonnull
    static final String REGEX = ColumnFormatter.REGEX + RowFormatter.REGEX;

    /**
     * Regular expression for matching coordinates - one or more letters representing column coordinate
     * plus one or more numbers representing row coordinate
     */
    @Nonnull
    private static final String PARSE_REGEX = "(" + ColumnFormatter.REGEX + ")(" + RowFormatter.REGEX + ")";

    /**
     * Matches a run of one or more letters followed by a run of one or more digits.
     * The run of letters is group 1 and the run of digits is group 2.
     */
    @Nonnull
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_REGEX);

    /**
     * Coordinates 0, 0 are often referenced, thus it makes sense to cache them
     */
    @Nonnull
    private static final CellCoordinatesInt A1 = new CellCoordinatesInt(0, 0);

    private final int row;
    private final int col;

    /**
     * Parse supplied address and convert it to cell coordinates
     *
     * @param address is cell address (as in Excel - e.g. A1)
     * @return cell coordinates corresponding to supplied address
     * @throws IllegalArgumentException if supplied address is not valid Excel cell reference, without sheet and with
     * no absolute position
     */
    @Nonnull
    static CellCoordinatesInt parse(String address) {
        if (address.isEmpty()) {
            throw new IllegalArgumentException("Empty string passed to cell reference parsing");
        }
        Matcher matcher = PARSE_PATTERN.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cell coordinates \"" + address + "\"");
        }
        try {
            return of(RowFormatter.parse(matcher.group(2)), ColumnFormatter.parse(matcher.group(1)));
        } catch (IllegalArgumentException ex) {
            // we want to see supplied string in error message
            throw new IllegalArgumentException(ex.getMessage() + "(coordinates \"" + address + "\")", ex);
        }
    }

    /**
     * Get cell coordinates with given address. Might cache instances, thus is used instead of constructor
     *
     * @param row is row index (zero based)
     * @param col is column index (zero based)
     * @return instance of CellCoordinates with given coordinates
     * @throws IllegalArgumentException if either coordinate is negative
     */
    @Nonnull
    static CellCoordinatesInt of(int row, int col) {
        if ((row == 0) && (col == 0)) {
            return A1;
        }
        return new CellCoordinatesInt(row, col);
    }

    private CellCoordinatesInt(int row, int col) {
        if (row < 0) {
            LOG.error("Row index must be positive, not {}", row);
            throw new IllegalArgumentException("Row index must be positive");
        }
        if (col < 0) {
            LOG.error("Column index must be positive, not {}", col);
            throw new IllegalArgumentException("Column index must be positive");
        }
        this.row = row;
        this.col = col;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public void appendAddress(StringBuilder builder) {
        ColumnFormatter.append(builder, getCol());
        RowFormatter.append(builder, getRow());
    }

    @Override
    @Nonnull
    public String getAddress() {
        StringBuilder builder = new StringBuilder();
        appendAddress(builder);
        return builder.toString();
    }

    @Override
    @Nonnull
    public Optional<CellCoordinates> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    @Nonnull
    public CellCoordinates shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return of(getRow() + rowShift, getCol() + colShift);
    }

    @Override
    @Nonnull
    public CellCoordinates shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellCoordinatesInt that = (CellCoordinatesInt) o;
        return getRow() == that.getRow() &&
                getCol() == that.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellCoordinatesInt{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
