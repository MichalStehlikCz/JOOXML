package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

final class AreaCellPathAbsoluteRecord implements AreaCellPath {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaCellPathAbsoluteRecord)) return false;
        AreaCellPathAbsoluteRecord that = (AreaCellPathAbsoluteRecord) o;
        return recordNr == that.recordNr &&
                child.equals(that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, recordNr);
    }

    @Override
    public String toString() {
        return "AreaCellPathAbsoluteRecord{" +
                "recordNr=" + recordNr +
                ", child=" + child +
                '}';
    }
}
