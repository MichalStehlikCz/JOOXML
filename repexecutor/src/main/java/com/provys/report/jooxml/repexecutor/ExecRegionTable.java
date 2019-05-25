package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecRegionTable extends ExecRegionBase {

    private final Logger LOG = LogManager.getLogger(ExecRegionTable.class);

    private final List<ExecRegion> records = new ArrayList<>(10);

    ExecRegionTable(String nameNm) {
        super(nameNm);
    }

    @Nonnull
    @Override
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathRecord)) {
            throw new IllegalArgumentException("Cannot evaluate cell path - record excepted");
        }
        int recordNr = ((CellPathRecord) path).getRecordNr();
        if (recordNr < 0) {
            recordNr += records.size();
            if (recordNr < 0) {
                LOG.debug("Cell reference returned empty - negative index bigger than number of rows ({}, {})",
                        recordNr - records.size(), this);
                return Optional.empty();
            }
        } else if (recordNr >= records.size()) {
            LOG.debug("Cell reference returned empty - index bigger than number of rows ({}, {})",
                    recordNr, this);
            return Optional.empty();
        }
        return records.get(recordNr).getCell(((CellPathRecord) path).getChildPath());
    }

    @Nonnull
    @Override
    public ExecRegion addArea(String nameNm, ContextCoordinates coordinates) {
        var subRegion = new ExecRegionArea(nameNm, coordinates);
        records.add(subRegion);
        return subRegion;
    }

    @Nonnull
    @Override
    public ExecRegion addRegion(String nameNm, int subRegions) {
        var subRegion = new ExecRegionRegion(nameNm, subRegions);
        records.add(subRegion);
        return subRegion;
    }

    @Nonnull
    @Override
    public ExecRegion addTable(String nameNm) {
        var subRegion = new ExecRegionTable(nameNm);
        records.add(subRegion);
        return subRegion;
    }

    /**
     * @param child is child execution region we want to find position of
     * @return position of child region in this table, -1 if child region is not found
     */
    public int getIndexOf(ExecRegion child) {
        return records.indexOf(child);
    }

    @Override
    public String toString() {
        return "ExecRegionTable{" +
                "nameNm='" + getNameNm() + '\'' +
                ", size=" + records.size() +
                "}";
    }
}
