package com.provys.report.jooxml.tplworkbook;

import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public interface TplCell {

    /**
     * @return row index of given cell; rows are indexed from 0, so this is one lower than row number visible in Excel
     */
    int getRowIndex();

    /**
     * @return column index; columns are indexed from 0
     */
    int getColIndex();

    /**
     * @return coordinates of given cell
     */
    @Nonnull
    CellCoordinates getCoordinates();

    /**
     * @return type of cell
     */
    @Nonnull
    CellType getCellType();

    /**
     * @return value present in given cell
     */
    @Nonnull
    CellValue getCellValue();

    /**
     * @return properties (style, comment, hyperlink) of given cell
     */
    @Nonnull
    Optional<CellProperties> getCellProperties();

    /**
     * @return list of cell references; returns empty list for anything else than formula cell, list of cell references
     * that are used in formula for formula cell
     */
    @Nonnull
    Collection<CellReference> getCellReferences();
}
