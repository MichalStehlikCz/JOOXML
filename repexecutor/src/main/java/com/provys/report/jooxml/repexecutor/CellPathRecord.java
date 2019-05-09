package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CellPathRecord implements CellPath {

    @Nonnull
    private final CellPath childPath;
    private final int recordNr;

    public CellPathRecord(CellPath childPath, int recordNr) {
        this.childPath = Objects.requireNonNull(childPath);
        this.recordNr = recordNr;
    }

    @Nonnull
    CellPath getChildPath() {
        return childPath;
    }

    int getRecordNr() {
        return recordNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellPathRecord)) return false;
        CellPathRecord that = (CellPathRecord) o;
        return getRecordNr() == that.getRecordNr() &&
                getChildPath().equals(that.getChildPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChildPath(), getRecordNr());
    }

    @Override
    public String toString() {
        return "CellPathRecord{" +
                "childPath=" + childPath +
                ", recordNr=" + recordNr +
                '}';
    }
}
