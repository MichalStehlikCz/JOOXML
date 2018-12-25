package com.provys.jooxml.report;

import com.provys.jooxml.report.TemplateCell;

import java.util.Collection;
import java.util.Optional;

public interface TemplateRow extends Iterable<TemplateCell> {

    /**
     * Return row index.
     *
     * @return row index of given row; note that row indexes are going from 0, unlike row numbers in excel tht are 1
     * based
     */
    int getRowNum();

    /**
     * Return number of cells in row.
     *
     * @return number of cells existing in row (e.g. ignores cells that do not have corresponding object)
     */
    int getPhysicalNumberOfCells();

    /**
     * Return cell with given index.
     *
     * @param i index of cell to be retrieved (starting from 0)
     * @return specified cell if it exists, empty optional if such cell does not exist in given row or is blank
     */
    Optional<TemplateCell> getCell(int i);

    /**
     * Return read-only collection of cells; only existing non-blank cells are returned.
     */
    Collection<TemplateCell> getCells();
}