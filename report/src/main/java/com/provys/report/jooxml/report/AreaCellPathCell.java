package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.CellPath;
import com.provys.report.jooxml.repexecutor.CellPathCell;
import com.provys.report.jooxml.repexecutor.ExecRegion;
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
    public Optional<CellPath> getCellPath(ExecRegion execRegion) {
        return Optional.of(new CellPathCell(cell));
    }
}
