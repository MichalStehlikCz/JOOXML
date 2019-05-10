package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.CellPath;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AreaCellPathRecord implements AreaCellPath {
    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(ExecRegionContext execRegionContext) {
        return Optional.empty();
    }
}
