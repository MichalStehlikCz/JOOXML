package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ExecRegionArea extends ExecRegionBase {

    private final ContextCoordinates coordinates;

    ExecRegionArea(String nameNm, ContextCoordinates coordinates) {
        super(nameNm);
        this.coordinates = new ContextCoordinates(coordinates);
    }

    @Nonnull
    @Override
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathCell)) {
            throw new IllegalArgumentException("Cannot evaluate CellPath - Cell expected in lowest level region");
        }
        return Optional.of(((CellPathCell) path).getCellReference().shiftBy(coordinates));
    }

    @Nonnull
    @Override
    public ExecRegion addArea(String nameNm, ContextCoordinates coordinates) {
        throw new RuntimeException("Cannot add sub-area to execution area");
    }

    @Nonnull
    @Override
    public ExecRegion addRegion(String nameNm, int subRegions) {
        throw new RuntimeException("Cannot add sub-region to execution area");
    }

    @Nonnull
    @Override
    public ExecRegion addTable(String nameNm) {
        throw new RuntimeException("Cannot add sub-table to execution area");
    }

    @Override
    public String toString() {
        return "ExecRegionArea{" +
                "nameNm=" + getNameNm() +
                ", coordinates=" + coordinates +
                "} ";
    }
}
