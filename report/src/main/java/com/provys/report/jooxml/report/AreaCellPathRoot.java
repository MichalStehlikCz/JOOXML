package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.CellPath;
import com.provys.report.jooxml.repexecutor.ExecRegionPath;
import com.provys.report.jooxml.repexecutor.ExecRegionRoot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Area cell path item that corresponds to DataStep report step.
 */
public class AreaCellPathRoot implements AreaCellPath {

    @Nonnull
    private final AreaCellPath child;

    AreaCellPathRoot(AreaCellPath child) {
        this.child = Objects.requireNonNull(child);
    }

    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath) {
        if ((execRegionPath != null) && (!(execRegionPath.getExecRegion() instanceof ExecRegionRoot))) {
                throw new RuntimeException("Root region expected to be on execution region path");
        }
        return child.getCellPath((execRegionPath == null) ? null : execRegionPath.getChild().orElse(null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaCellPathRoot)) return false;
        AreaCellPathRoot that = (AreaCellPathRoot) o;
        return child.equals(that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child);
    }

    @Override
    public String toString() {
        return "AreaCellPathRoot{" +
                "child=" + child +
                '}';
    }
}
