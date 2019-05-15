package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

final class AreaCellPathRelativeRecord implements AreaCellPath {

    @Nonnull
    private final AreaCellPath child;
    private final int recordNr;

    AreaCellPathRelativeRecord(AreaCellPath child, int recordNr) {
        this.child = Objects.requireNonNull(child);
        this.recordNr = recordNr;
    }

    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath) {
        if (execRegionPath == null) {
            throw new RuntimeException("Accessing relative cell path without supplied execution region path " + this);
        }
        var execRegion = execRegionPath.getExecRegion();
        if (!(execRegion instanceof ExecRegionTable)) {
            throw new RuntimeException("Relative path cell path aligned with non-table region " + execRegion);
        }
        var childExecRegionPath = execRegionPath.getChild().orElseThrow(
                () -> new RuntimeException("Accessing relative cell path with no child record specified"));
        var childExecRegion = childExecRegionPath.getExecRegion();
        var actualRecordNr = ((ExecRegionTable) execRegion).getIndexOf(childExecRegion);
        if (actualRecordNr == -1) {
            throw new RuntimeException("Child region not found in table when evaluating relative path cell (table " +
                    execRegion + ", child " + childExecRegion + ')');
        }
        final var effectiveRecordNr = recordNr + actualRecordNr;
        if (effectiveRecordNr < 0) {
            // referenced region is before start of table...
            return Optional.empty();
        }
        return child.getCellPath(childExecRegionPath).
                map(childPath -> new CellPathRecord(childPath, effectiveRecordNr));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaCellPathRelativeRecord)) return false;
        AreaCellPathRelativeRecord that = (AreaCellPathRelativeRecord) o;
        return recordNr == that.recordNr &&
                child.equals(that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, recordNr);
    }

    @Override
    public String toString() {
        return "AreaCellPathRelativeRecord{" +
                "recordNr=" + recordNr +
                ", child=" + child +
                '}';
    }
}
