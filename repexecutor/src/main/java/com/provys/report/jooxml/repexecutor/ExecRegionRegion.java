package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

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
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathRegion)) {
            throw new IllegalArgumentException("Region path expected");
        }
        return subRegions.get(((CellPathRegion) path).getRegion()).getCell(((CellPathRegion) path).getChildPath());
    }

    @Override
    public ExecRegion addArea(String nameNm, ContextCoordinates coordinates) {
        var area = new ExecRegionArea(nameNm, coordinates);
        addSubRegion(area);
        return area;
    }

    @Override
    public ExecRegion addRegion(String nameNm, int subRegions) {
        var region = new ExecRegionRegion(nameNm, subRegions);
        addSubRegion(region);
        return region;
    }

    @Override
    public ExecRegion addTable(String nameNm) {
        var table = new ExecRegionTable(nameNm);
        addSubRegion(table);
        return table;
    }
}
