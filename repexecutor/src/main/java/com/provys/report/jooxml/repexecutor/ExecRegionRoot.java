package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * It is root exec region - created by report executor and passed to root processor. It has single child (corresponding
 * to root step / processor) and passes cell address evaluation to this child. It has fixed internal name ROOT.
 */
public class ExecRegionRoot implements ExecRegion {

    @Nullable
    private ExecRegion rootRegion = null;

    /**
     * @return ROOT as it is fixed name for this execution region
     */
    @Nonnull
    @Override
    public String getNameNm() {
        return "ROOT";
    }

    /**
     * Pass cell evaluation to child.
     *
     * @param path is path to specified cell
     * @return cell reference corresponding to specified cell; return empty optional if there is no child region
     */
    @Nonnull
    @Override
    public Optional<CellReference> getCell(CellPath path) {
        return (rootRegion == null) ? Optional.empty() : rootRegion.getCell(path);
    }

    @Nonnull
    @Override
    public ExecRegion addArea(String nameNm, ContextCoordinates coordinates) {
        if (rootRegion != null) {
            throw new RuntimeException("Cannot create sub-area - root region is already assigned");
        }
        rootRegion = new ExecRegionArea(nameNm, coordinates);
        return rootRegion;
    }

    @Nonnull
    @Override
    public ExecRegion addRegion(String nameNm, int subRegions) {
        if (rootRegion != null) {
            throw new RuntimeException("Cannot create sub-region - root region is already assigned");
        }
        rootRegion = new ExecRegionRegion(nameNm, subRegions);
        return rootRegion;
    }

    @Nonnull
    @Override
    public ExecRegion addTable(String nameNm) {
        if (rootRegion != null) {
            throw new RuntimeException("Cannot create sub-table - root region is already assigned");
        }
        rootRegion = new ExecRegionTable(nameNm);
        return rootRegion;
    }
}
