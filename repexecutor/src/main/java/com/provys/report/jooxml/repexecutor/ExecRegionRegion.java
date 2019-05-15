package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Holds data about area populated by report region iteration
 */
public class ExecRegionRegion extends ExecRegionBase {

    private final Map<String, ExecRegion> subRegions;

    public ExecRegionRegion(String nameNm, int subRegions) {
        super(nameNm);
        this.subRegions = new HashMap<>(subRegions);
    }

    private void addSubRegion(ExecRegion subRegion) {
        if (subRegions.put(subRegion.getNameNm(), subRegion) != null) {
            throw new IllegalArgumentException("Duplicate subregion detected during execution");
        }
    }

    @Override
    @Nonnull
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathRegion)) {
            throw new IllegalArgumentException("Region path expected");
        }
        var subRegion = subRegions.get(((CellPathRegion) path).getRegionNm());
        if (subRegion == null) {
            throw new RuntimeException("Subregion " + ((CellPathRegion) path).getRegionNm() + " not found in region " +
                    getNameNm());
        }
        return subRegion.getCell(((CellPathRegion) path).getChildPath());
    }

    @Override
    @Nonnull
    public ExecRegion addArea(String nameNm, ContextCoordinates coordinates) {
        var area = new ExecRegionArea(nameNm, coordinates);
        addSubRegion(area);
        return area;
    }

    @Override
    @Nonnull
    public ExecRegion addRegion(String nameNm, int subRegions) {
        var region = new ExecRegionRegion(nameNm, subRegions);
        addSubRegion(region);
        return region;
    }

    @Override
    @Nonnull
    public ExecRegion addTable(String nameNm) {
        var table = new ExecRegionTable(nameNm);
        addSubRegion(table);
        return table;
    }

    @Nonnull
    public Optional<ExecRegion> getSubRegion(String nameNm) {
        return Optional.ofNullable(subRegions.get(nameNm));
    }

    @Override
    public String toString() {
        return "ExecRegionRegion{" +
                "nameNm='" + getNameNm() + '\'' +
                "}";
    }
}
