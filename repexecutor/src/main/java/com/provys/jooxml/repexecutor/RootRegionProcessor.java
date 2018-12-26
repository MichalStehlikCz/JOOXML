package com.provys.jooxml.repexecutor;

/**
 * Root processor. Has no parent and when its line pointer is moved, rows in supporting worksheet are flushed.
 */
public class RootRegionProcessor implements RegionProcessor {

    private ExecRegionRegion execRegion;
    private int currentRow = 0;

    @Override
    public ExecRegionRegion getExecRegion() {
        return execRegion;
    }

    @Override
    public void addExecSubRegion(ExecRegionRegion execSubRegion) {
        execRegion.addSubRegion(execSubRegion);
    }

    @Override
    public void process(RepWorkbook workbook) {

    }
}
