package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.*;
import com.provys.report.jooxml.repworkbook.RepRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.*;
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
    public StepProcessor getProcessor(StepContext stepContext, ExecRegionContext execRegionContext) {
        return new RowCellAreaProcessor(this, stepContext, execRegionContext);
    }

    private static class RowCellAreaProcessor extends StepProcessorAncestor<RowCellArea> {

        private static final Logger LOG = LogManager.getLogger(RowCellAreaProcessor.class);

        RowCellAreaProcessor(RowCellArea step, StepContext stepContext, ExecRegionContext parentRegionContext) {
            // unlike other step processors that append their region to parentRegion immediately, are processor keeps
            // parent region and only attaches child region on execution
            // it might mess up order in which sub-regions are added, but it shouldn't matter as regions are identified
            // by internal names in parent region (no order) and order only matters in table, but table only has single
            // child and if it is area, all rows will be processed in execution phase
            super(step, stepContext, parentRegionContext);
        }

        @Override
        public Stream<StepProcessor> apply() {
            return Stream.of(this);
        }

        @Override
        public void execute() {
            LOG.trace("Execute RowCellAreaProcessor {}", this::getStep);
            ContextCoordinates coordinates = getStepContext().getCoordinates();
            var execRegionContext = getExecRegionContext().addArea(getStep().getNameNm(), coordinates);
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
                            , regionCell.getEffectiveValue(getStepContext().getData(), execRegionContext,
                                    getStepContext().getReportContext().getCellPathReplacer())
                            , regionCell.getProperties().orElse(null));
                }
            }
            // and move coordinates for the next area
            coordinates.incRowBy(getStep().getHeight());
        }
    }
}
