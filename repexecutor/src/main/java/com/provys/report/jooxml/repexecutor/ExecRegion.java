package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import java.util.Optional;

/**
 * Holds data about region structure during execution.
 * Used for mapping cell references to actual cells in generated sheet
 */
public interface ExecRegion {
    Optional<CellReference> getCell(CellPath path);
}
