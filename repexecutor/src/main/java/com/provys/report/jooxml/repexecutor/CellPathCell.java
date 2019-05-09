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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellPathCell)) return false;
        CellPathCell that = (CellPathCell) o;
        return cell.equals(that.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }

    @Override
    public String toString() {
        return "CellPathCell{" +
                "cell=" + cell +
                '}';
    }
}
