package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellAddress;
import com.provys.report.jooxml.workbook.CellCoordinates;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellAddressImpl implements CellAddress {

    /**
     * Matches optional sheet reference (anything followed by !), followed by a run of one or more letters followed by
     * a run of one or more digits.
     * Sheet reference is optional, column and row references are mandatory.
     * Sheet reference is group 1, run of letters is group 2 and the run of digits is group 3.
     */
    private static final Pattern CELL_ADDRESS_PATTERN = Pattern.compile(SheetNameFormatter.getRegExp() +
                    ColumnFormatter.getRegExp() + "([0-9]+)",
            Pattern.CASE_INSENSITIVE);

    private final String sheetName;
    private final CellCoordinates coordinates;

    /**
     * Parse cell address and return corresponding CellAddress instance.
     *
     * @param address is cell address compatible with Excel, with no absolute row nor cell coordinate. Sheet part is
     *               optional
     * @return CellAddress corresponding to supplied address
     * @throws IllegalArgumentException if supplied string is not valid Excell cell address or if it contains absolute
     * row or column reference ($ prefix)
     */
    static CellAddressImpl parse(String address) {
        Objects.requireNonNull(address);
        Matcher matcher = CELL_ADDRESS_PATTERN.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cell address \"" + address + "\"");
        }
        try {
            return of(SheetNameFormatter.parse(matcher.group(1), false).orElse(null),
                    Workbooks.getCellCoordinates(Integer.parseInt(matcher.group(3)) - 1,
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
     * @return instance of CellAddress with given coordinates
     * @throws IllegalArgumentException if sheet name is supplied, but empty
     */
    static CellAddressImpl of(@Nullable String sheetName, CellCoordinates coordinates) {
        return new CellAddressImpl(sheetName, coordinates);
    }

    /**
     * Address constructor; use static method of instead
     */
    CellAddressImpl(@Nullable String sheetName, CellCoordinates coordinates) {
        if (sheetName != null) {
            if (sheetName.equals("")) {
                throw new IllegalArgumentException("Empty sheet name sent to CellAddress constructor");
            }
        }
        this.sheetName = sheetName;
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    @Override
    public Optional<String> getSheetName() {
        return Optional.ofNullable(sheetName);
    }

    @Override
    public CellCoordinates getCoordinates() {
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
        getSheetName().ifPresent(sheetName -> SheetNameFormatter.append(builder, sheetName));
        ColumnFormatter.append(builder, getCol());
        builder.append(getRow() + 1);
    }

    @Override
    public String getAddress() {
        StringBuilder builder = new StringBuilder();
        appendAddress(builder);
        return builder.toString();
    }

    @Override
    public Optional<CellAddress> shiftByOrEmpty(int rowShift, int colShift) {
        if ((getRow() < -rowShift) || (getCol() < -colShift)) {
            return Optional.empty();
        }
        return Optional.of(shiftBy(rowShift, colShift));
    }

    @Override
    public CellAddress shiftBy(int rowShift, int colShift) {
        if ((rowShift == 0) && (colShift == 0)) {
            return this;
        }
        return CellAddressImpl.of(sheetName,
                Workbooks.getCellCoordinates(getRow() + rowShift, getCol() + colShift));
    }

    @Override
    public CellAddress shiftBy(CellCoordinates shift) {
        return shiftBy(shift.getRow(), shift.getCol());
    }
}
