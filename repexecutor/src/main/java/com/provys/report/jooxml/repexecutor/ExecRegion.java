package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Holds data about region structure during execution.
 * Used for mapping cell references to actual cells in generated sheet
 */
public interface ExecRegion {

    /**
     * @return internal name of this region
     */
    @Nonnull
    String getNameNm();

    /**
     * Evaluates cell reference to cell on specified path, based on fact that this region is being processed
     *
     * @param path is path to specified cell
     * @return cell reference that can be used to adress given cell in resulting workbook
     */
    @Nonnull
    Optional<CellReference> getCell(CellPath path);

    /**
     * Add new area execution region under this region. Area region represents leaf region, mapping to area in target
     * excel sheet
     *
     * @param nameNm is internal name of area to be added
     * @param coordinates represent upper left corner of area where content of this area will be placed (and thus index
     *                   that has to be used for any coordinate within given area)
     * @return reference to new child area
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nonnull
    ExecRegion addArea(String nameNm, ContextCoordinates coordinates);

    /**
     * Add new non-leaf level region under this region. New region will pose as parent for one or more regions, but does
     * not directly generate any area in target sheet.
     *
     * @param nameNm is internal name of subregion being inserted
     * @param subRegions is expected number of subregions (to pre-allocate space)
     * @return reference to new child region
     */
    @Nonnull
    ExecRegion addRegion(String nameNm, int subRegions);

    /**
     * Add new table region. Table should have single child region, but this child region is repeated multiple times
     * and table region uses line numbers to address individual subregions.
     *
     * @param nameNm is internal name of new child table region
     * @return reference to new child region
     */
    @Nonnull
    ExecRegion addTable(String nameNm);
}
