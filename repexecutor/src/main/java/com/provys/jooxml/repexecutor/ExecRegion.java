package com.provys.jooxml.repexecutor;

import org.apache.poi.ss.util.CellReference;

import java.util.Optional;

/**
 * Holds data about region structure during execution.
 * Used for mapping cell references to actual cells in generated sheet
 */
public interface ExecRegion {
    Optional<CellReference> getCell(CellPath path);
}
