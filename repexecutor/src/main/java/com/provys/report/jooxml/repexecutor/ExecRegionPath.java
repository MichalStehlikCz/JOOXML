package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Class represents path from root ExecRegion to one currently processed. It is basically reverse version of
 * ExecRegionContext and it is created from ExecRegionContext to enable conversion of AreaCellPath to CellPath
 */
public class ExecRegionPath {
    @Nonnull
    private final ExecRegion execRegion;
    @Nullable
    private final ExecRegionPath child;

    ExecRegionPath(ExecRegion execRegion, @Nullable ExecRegionPath child) {
        this.execRegion = execRegion;
        this.child = child;
    }

    /**
     * @return value of field execRegion
     */
    @Nonnull
    public ExecRegion getExecRegion() {
        return execRegion;
    }

    /**
     * @return value of field child
     */
    @Nonnull
    public Optional<ExecRegionPath> getChild() {
        return Optional.ofNullable(child);
    }
}
