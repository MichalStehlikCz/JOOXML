package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Gives access to instances of CellReference type
 */
public interface CellReferenceFactory {
    @Nonnull
    CellReference of(@Nullable String sheetName, int row, int col, boolean rowAbsolute, boolean colAbsolute);
    @Nonnull
    CellReference of(int row, int col, boolean rowAbsolute, boolean colAbsolute);
    @Nonnull
    CellReference of(@Nullable String sheetName, CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute);
    @Nonnull
    CellReference of(CellCoordinates coordinates, boolean rowAbsolute, boolean colAbsolute);
    @Nonnull
    CellReference of(@Nullable String sheetName, int row, int col);
    @Nonnull
    CellReference of(int row, int col);
    @Nonnull
    CellReference of(@Nullable String sheetName, CellCoordinates coordinates);
    @Nonnull
    CellReference of(CellCoordinates coordinates);

    /**
     * @return regular expression matching cell reference
     */
    @Nonnull
    String getRegex();

    /**
     * Parse supplied address into cell reference
     *
     * @param address is string representing cell reference
     * @return parsed cell reference
     */
    @Nonnull
    CellReference parse(String address);
}
