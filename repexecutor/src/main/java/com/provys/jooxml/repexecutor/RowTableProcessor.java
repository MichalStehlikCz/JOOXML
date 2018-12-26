package com.provys.jooxml.repexecutor;

import java.io.IOException;

/**
 * Processor used to generate records for individual records in associated dataset
 */
public class RowTableProcessor implements RegionProcessor {
    RegionProcessor parent;
    ReportRegion region;
    ExecRegionTable execRegion;

    @Override
    public ExecRegion getExecRegion() {
        return execRegion;
    }

    @Override
    public void process(RepWorkbook workbook) {
        DataStream dataStream = region.getDataSource().orElseThrow(
                () -> new IllegalArgumentException("Cannot create table based on region without data source"))
                .getDataStream(parent.getDataContext());
        RowSingleProcessor = new RowSingleProcessor();

    }

    @Override
    public void close() throws IOException {

    }
}
