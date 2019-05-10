package com.provys.report.jooxml.repexecutor;

/**
 * Class represents path to of cell in parsed report - going from root region through all subregions to region that
 * actually populates cell with record number in each region; on lowest level, it keeps CellReference with row and
 * column indices relative to paths region.
 */
public interface CellPath {}
