package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;
import com.provys.report.jooxml.repworkbook.RepRow;
import com.provys.report.jooxml.repexecutor.StepContext;

import java.util.Objects;

public class RowCellAreaProcessor extends StepProcessorAncestor {

    private final RowCellArea region;

    RowCellAreaProcessor(RowCellArea region, StepContext stepContext) {
        super(stepContext);
        this.region = Objects.requireNonNull(region);
    }

    @Override
    public void execute() {
        ContextCoordinates coordinates = getStepContext().getCoordinates();
        // populate cells in sheet with cells from region
        for (Row regionRow : region.getRows()) {
            RepRow targetRow;
            if (region.isTopLevel()) {
                targetRow = coordinates.getSheet().createRow(coordinates.getRowIndex() + regionRow.getRowIndex()
                        , regionRow.getRowProperties());
            } else {
                targetRow = coordinates.getSheet().getOrCreateRow(
                        coordinates.getRowIndex() + regionRow.getRowIndex());
            }
            for (AreaCell regionCell : regionRow.getCells()) {
                targetRow.addCell(coordinates.getColumnIndex() + regionCell.getColIndex()
                        , regionCell.getEffectiveValue(getStepContext().getData())
                        , regionCell.getProperties().orElse(null));
            }
        }
        // and move coordinates for the next area
        coordinates.incRowBy(region.getHeight());
    }
}
