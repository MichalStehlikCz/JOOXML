package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;
import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AreaCellPathCell implements AreaCellPath {

    @Nonnull
    private final CellReference cell;

    public AreaCellPathCell(CellReference cell) {
        this.cell = cell;
    }

    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(ExecRegionContext execRegionContext) {
        return Optional.of(new CellPathCell(cell));
    }
}
