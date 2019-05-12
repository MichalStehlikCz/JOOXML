package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class AreaCellPathAbsoluteRecord implements AreaCellPath {

    @Nonnull
    private final AreaCellPath child;
    private final int recordNr;

    AreaCellPathAbsoluteRecord(AreaCellPath child, int recordNr) {
        this.child = Objects.requireNonNull(child);
        this.recordNr = recordNr;
    }

    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath) {
        return child.getCellPath((execRegionPath == null) ? null : execRegionPath.getChild().orElse(null)).
                map(childPath -> new CellPathRecord(childPath, recordNr));
    }
}
