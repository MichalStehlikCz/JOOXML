package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;
import com.provys.report.jooxml.repexecutor.RepWRow;
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
            RepWRow targetRow;
            if (region.isTopLevel()) {
                targetRow = coordinates.getSheet().createRow(coordinates.getRowIndex() + regionRow.getRowIndex()
                        , regionRow.getRowProperties());
            } else {
                targetRow = coordinates.getSheet().getOrCreateRow(
                        coordinates.getRowIndex() + regionRow.getRowIndex());
            }
            for (AreaCell regionCell : regionRow.getCells()) {
                switch (regionCell.getCellType()) {
                    case FORMULA:
                        targetRow.addFormulaCell(coordinates.getColumnIndex() + regionCell.getColumnIndex()
                                , regionCell.getCellFormula(), regionCell.getCellStyleIndex()
                                , regionCell.getCellComment(), regionCell.getHyperlink());
                        break;
                    case BLANK:
                    case STRING:
                        targetRow.addStringCell(coordinates.getColumnIndex() + regionCell.getColumnIndex()
                                , regionCell.getStringEffectiveValue(getStepContext().getData())
                                , regionCell.getCellStyleIndex()
                                , regionCell.getCellComment(), regionCell.getHyperlink());
                        break;
                    case NUMERIC:
                        targetRow.addNumericCell(coordinates.getColumnIndex() + regionCell.getColumnIndex()
                                , regionCell.getNumericEffectiveValue(getStepContext().getData())
                                , regionCell.getCellStyleIndex()
                                , regionCell.getCellComment(), regionCell.getHyperlink());
                        break;
                    case BOOLEAN:
                        targetRow.addBooleanCell(coordinates.getColumnIndex() + regionCell.getColumnIndex()
                                , regionCell.getBooleanEffectiveValue(getStepContext().getData())
                                , regionCell.getCellStyleIndex()
                                , regionCell.getCellComment(), regionCell.getHyperlink());
                        break;
                    case ERROR:
                        targetRow.addErrorCell(coordinates.getColumnIndex() + regionCell.getColumnIndex()
                                , regionCell.getErrorCellValue(), regionCell.getCellStyleIndex()
                                , regionCell.getCellComment(), regionCell.getHyperlink());
                        break;
                    default:
                        throw new RuntimeException("Unexpected cell type populating cells in area");
                }
            }
        }
        // and move coordinates for the next area
        coordinates.incRowBy(region.getHeight());
    }
}
