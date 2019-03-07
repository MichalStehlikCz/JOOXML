package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CellPathCell implements CellPath {

    @Nonnull
    private final CellReference cell;

    public CellPathCell(CellReference cell) {
        this.cell = Objects.requireNonNull(cell);
    }

    @Nonnull
    CellReference getCellReference() {
        return cell;
    }
}
