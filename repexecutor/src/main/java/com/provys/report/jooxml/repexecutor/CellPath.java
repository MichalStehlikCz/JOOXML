package com.provys.report.jooxml.repexecutor;

import org.apache.poi.ss.util.CellReference;

import java.util.Optional;

/**
 * Class represents address of cell in report - going from root region through all subregions to region that actually
 * populates cell with record number in each region; on lowest level, it keeps CellReference with row and column
 * indices relative to paths region.
 */
public interface CellPath {

}
