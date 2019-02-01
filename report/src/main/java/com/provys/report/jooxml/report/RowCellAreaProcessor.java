package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;
import com.provys.report.jooxml.repworkbook.RepRow;
import com.provys.report.jooxml.repexecutor.StepContext;

import java.util.Objects;

class RowCellAreaProcessor extends StepProcessorAncestor<RowCellArea> {

    RowCellAreaProcessor(RowCellArea step, StepContext stepContext) {
        super(step, stepContext);
    }

    @Override
    public void execute() {
        ContextCoordinates coordinates = getStepContext().getCoordinates();
        // populate cells in sheet with cells from region
        for (Row regionRow : getStep().getRows()) {
            RepRow targetRow;
            if (getStep().isTopLevel()) {
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
        coordinates.incRowBy(getStep().getHeight());
    }
}
