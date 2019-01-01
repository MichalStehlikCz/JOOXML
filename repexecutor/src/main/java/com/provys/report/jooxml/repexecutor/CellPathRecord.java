package com.provys.report.jooxml.repexecutor;

import java.util.Objects;

public class CellPathRecord implements CellPath {
    private final CellPath childPath;
    private final int recordNr;

    public CellPathRecord(CellPath childPath, int recordNr) {
        this.childPath = Objects.requireNonNull(childPath);
        if (recordNr < 0) {
            throw new IllegalArgumentException("Record number cannot be negative");
        }
        this.recordNr = recordNr;
    }

    public CellPath getChildPath() {
        return childPath;
    }

    public int getRecordNr() {
        return recordNr;
    }
}
