package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import java.util.*;

/**
 * Holds data about area populated by report region iteration
 */
public class ExecRegionRegion implements ExecRegion {
    private final Map<String, ExecRegion> subRegions;

    public ExecRegionRegion(int subRegions) {
        this.subRegions = new HashMap<>(subRegions);
    }

    public void addSubRegion(String nameNm, ExecRegion subRegion) {
        if (subRegions.put(Objects.requireNonNull(nameNm), Objects.requireNonNull(subRegion)) != null) {
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
}
