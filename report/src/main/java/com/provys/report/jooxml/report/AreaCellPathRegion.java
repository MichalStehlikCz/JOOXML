package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath) {
        var childRegionPath = (execRegionPath == null) ? null : execRegionPath.getChild().orElse(null);
        if (childRegionPath != null) {
            // we must verify if this region corresponds to one on path - otherwise, we cannot send path downstream
            var execRegion = execRegionPath.getExecRegion();
            if (!(execRegion instanceof ExecRegionRegion)) {
                throw new RuntimeException("Execution region aligned to record cell path is not region " + execRegion);
            }
            var subRegion = ((ExecRegionRegion) execRegion).getSubRegion(regionNm).orElse(null);
            if (subRegion != childRegionPath.getExecRegion()) {
                childRegionPath = null;
            }
        }
        return child.getCellPath(childRegionPath).map(childPath -> new CellPathRegion(childPath, regionNm));
    }
}
