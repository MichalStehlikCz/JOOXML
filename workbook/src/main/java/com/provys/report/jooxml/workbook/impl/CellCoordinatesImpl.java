package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellCoordinatesImpl implements CellCoordinates {

    private static final Logger LOG = LogManager.getLogger(CellCoordinatesImpl.class.getName());

    /**
     * Regular expression for matching coordinates - one or more letters representing column coordinate
     * plus one or more numbers representing row coordinate
     */
    public static final String REGEXP = ColumnFormatter.REGEXP + RowFormatter.REGEXP;

    /**
     * Regular expression for matching coordinates - one or more letters representing column coordinate
     * plus one or more numbers representing row coordinate
     */
    private static final String PARSE_REGEXP = "(" + ColumnFormatter.REGEXP + ")(" + RowFormatter.REGEXP + ")";

    /**
     * Matches a run of one or more letters followed by a run of one or more digits.
     * The run of letters is group 1 and the run of digits is group 2.
     */
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_REGEXP);

    /**
     * Coordinates 0, 0 are often referenced, thus it makes sense to cache them
     */
    private static final CellCoordinatesImpl A1 = new CellCoordinatesImpl(0, 0);

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
    static CellCoordinatesImpl parse(String address) {
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
    static CellCoordinatesImpl of(int row, int col) {
        if ((row == 0) && (col == 0)) {
            return A1;
        }
        return new CellCoordinatesImpl(row, col);
    }

    private CellCoordinatesImpl(int row, int col) {
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
    public String getAddress() {
        StringBuilder builder = new StringBuilder();
        appendAddress(builder);
        return builder.toString();
    }

    @Override
    public Optional<CellCoordinates> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    public CellCoordinates shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return of(getRow() + rowShift, getCol() + colShift);
    }

    @Override
    public CellCoordinates shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellCoordinatesImpl that = (CellCoordinatesImpl) o;
        return getRow() == that.getRow() &&
                getCol() == that.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    @Override
    public String toString() {
        return "CellCoordinatesImpl{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
