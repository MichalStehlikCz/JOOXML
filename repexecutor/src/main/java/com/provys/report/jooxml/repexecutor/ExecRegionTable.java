package com.provys.report.jooxml.repexecutor;

import org.apache.poi.ss.util.CellReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecRegionTable implements ExecRegion {

    private final List<ExecRegion> records = new ArrayList<>(10);

    @Override
    public Optional<CellReference> getCell(CellPath path) {
        if (!(path instanceof CellPathRecord)) {
            throw new IllegalArgumentException("Cannot evaluate cell path - record excepted");
        }
        int recordNr = ((CellPathRecord) path).getRecordNr();
        if (recordNr == Integer.MAX_VALUE) {
            if (records.size() == 0) {
                return Optional.empty();
            }
            recordNr = records.size() - 1;
        } else if (recordNr > records.size()) {
            return Optional.empty();
        }
        return records.get(recordNr).getCell(((CellPathRecord) path).getChildPath());
    }

}
