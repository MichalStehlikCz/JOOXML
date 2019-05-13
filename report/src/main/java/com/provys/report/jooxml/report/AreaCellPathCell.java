package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;
import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

final class AreaCellPathCell implements AreaCellPath {

    @Nonnull
    private final CellReference cell;

    AreaCellPathCell(CellReference cell) {
        this.cell = cell;
    }

    @Nonnull
    @Override
    public Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath) {
        return Optional.of(new CellPathCell(cell));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaCellPathCell)) return false;
        AreaCellPathCell that = (AreaCellPathCell) o;
        return cell.equals(that.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }
}
