package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ContextCoordinates;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;
import com.provys.report.jooxml.repworkbook.RepRow;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

class RowCellArea extends Step {

    private final boolean topLevel; // indicates that it is top level region, that should initiate rows in sheet
    private final int height;
    @Nonnull
    private final List<Row> rows;

    RowCellArea(String nameNm, boolean topLevel, int height, Collection<Row> rows) {
       super(nameNm);
       this.topLevel = topLevel;
       this.height = height;
       this.rows = new ArrayList<>(rows);
    }

    /**
     * @return all rows holding cells ith binding in given area
     */
    @Nonnull
    private List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }

    /**
     * @return if region is top level region (e.g. it should create rows as there is no parent that can create rows
     * instead)
     */
    private boolean isTopLevel() {
        return topLevel;
    }

    /**
     * @return height of given area
     */
    private int getHeight() {
        return height;
    }

    @Override
    public int getNeededProcessorApplications() {
        // terminal step, no mapping needed
        return 0;
    }

    @Nonnull
    @Override
    public StepProcessor getProcessor(StepContext stepContext) {
        return new RowCellAreaProcessor(this, stepContext);
    }

    private static class RowCellAreaProcessor extends StepProcessorAncestor<RowCellArea> {

        RowCellAreaProcessor(RowCellArea step, StepContext stepContext) {
            super(step, stepContext);
        }

        @Override
        public Stream<StepProcessor> apply() {
            return Stream.of(this);
        }

        @Override
        public void execute() {
            ContextCoordinates coordinates = getStepContext().getCoordinates();
            // populate cells in sheet with cells from region
            for (Row regionRow : getStep().getRows()) {
                RepRow targetRow;
                if (getStep().isTopLevel()) {
                    targetRow = coordinates.getSheet().createRow(coordinates.getRowIndex() + regionRow.getRowIndex()
                            , regionRow.getRowProperties().orElse(null));
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
}
