package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;

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
    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }

    /**
     * @return if region is top level region (e.g. it should create rows as there is no parent that can create rows
     * instead)
     */
    boolean isTopLevel() {
        return topLevel;
    }

    /**
     * @return height of given area
     */
    int getHeight() {
        return height;
    }

    @Nonnull
    @Override
    public Stream<StepProcessor> addStepProcessing(Stream<StepProcessor> pipeline) {
        return pipeline;
    }

    @Nonnull
    @Override
    public Function<StepContext, StepProcessor> getProcessorSupplier() {
        return (context -> new RowCellAreaProcessor(this, context));
    }
}
