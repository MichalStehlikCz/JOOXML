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
}
