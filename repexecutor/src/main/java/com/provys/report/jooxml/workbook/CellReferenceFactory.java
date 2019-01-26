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
    @Nonnull
    CellReference parse(String address);
}
