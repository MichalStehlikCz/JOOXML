package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public class AreaCellPathRegion implements AreaCellPath {
    @Nonnull
    private final String regionNm;
    @Nonnull
    private final AreaCellPath child;

    AreaCellPathRegion(String regionNm, AreaCellPath child) {
        this.regionNm = Objects.requireNonNull(regionNm);
        this.child = Objects.requireNonNull(child);
    }
    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(ExecRegionContext execRegionContext) {
        ExecRegion childRegion;
//        if (execRegion instanceof ExecRegionRegion) {
//            childRegion = ((ExecRegionRegion) execRegion).

//        }
//        var childPath = child.getCellPath(execRegion);
//        return childPath.map(cellPath -> new CellPathRegion(cellPath, execRegion.getNameNm()));
        return Optional.empty();
    }
}
