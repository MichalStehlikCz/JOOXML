package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface CellAddress extends com.provys.report.jooxml.workbook.CellAddress {

    /**
     * Matches optional sheet reference (anything followed by !), followed by a run of one or more letters followed by
     * a run of one or more digits.
     * Sheet reference is optional, column and row references are mandatory.
     */
    @Nonnull
    String REGEXP = "(?:(?:" + SheetNameFormatter.REGEXP + ')' + SheetNameFormatter.SHEET_NAME_DELIMITER + ")?"
            + ColumnFormatter.REGEXP + RowFormatter.REGEXP;

    @Nonnull
    static CellAddress of(@Nullable String sheetName, int row, int col) {
        return CellAddressImpl.of(sheetName, row, col);
    }

    @Nonnull
    static  CellAddress of(int row, int col) {
        return CellAddressImpl.of(row, col);
    }

    @Nonnull
    static CellAddress of(@Nullable String sheetName, CellCoordinates coordinates) {
        return CellAddressImpl.of(sheetName, coordinates);
    }

    @Nonnull
    static  CellAddress of(CellCoordinates coordinates) {
        return CellAddressImpl.of(coordinates);
    }

    /**
     * Parse cell address and return corresponding CellAddress instance.
     *
     * @param address is cell address compatible with Excel, with no absolute row nor cell coordinate. Sheet part is
     *               optional
     * @return CellAddress corresponding to supplied address
     * @throws IllegalArgumentException if supplied string is not valid Excel cell address or if it contains absolute
     * row or column reference ($ prefix)
     */
    @Nonnull
    static CellAddress parse(String address) {
        return CellAddressImpl.parse(address);
    }

    @Override
    @Nonnull
    Optional<String> getSheetName();

    @Override
    @Nonnull
    CellCoordinates getCoordinates();

    @Override
    int getRow();

    @Override
    int getCol();

    @Override
    void appendAddress(StringBuilder builder);

    @Override
    @Nonnull String getAddress();

    @Override
    @Nonnull
    Optional<? extends CellAddress> shiftByOrEmpty(int rowShift, int colShift);

    @Override
    @Nonnull
    CellAddress shiftBy(int rowShift, int colShift);

    @Override
    @Nonnull
    CellAddress shiftBy(CellCoordinates shift);
}
