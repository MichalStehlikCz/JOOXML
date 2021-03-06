package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

class RowParentAreaBuilder extends RowRegionBuilder<RowParentAreaBuilder> {

    @Nonnull
    private final List<RowStepBuilder> children = new ArrayList<>(5);

    RowParentAreaBuilder(@Nullable StepBuilder parent) {
        super(parent);
    }

    /**
     * @return effective first row covered by this builder; if the first row has been explicitly set, it is returned.
     * Otherwise if there are child steps, it returns the first row of the first child
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffFirstRow() {
        return getFirstRow().or(() -> (!(children.isEmpty()) ? children.get(0).getEffFirstRow() : Optional.empty()));
    }

    /**
     * @return effective last row covered by this builder; if the last row has been explicitly set, it is returned.
     * Otherwise if there are child steps, it returns the last row of the last child
     */
    @Nonnull
    @Override
    public Optional<Integer> getEffLastRow() {
        return getLastRow().
                or(() -> (children.isEmpty()) ? Optional.empty() : (children.get(children.size()-1).getEffLastRow()));
    }

    /**
     * Add child to list of children
     */
    void addChild(RowStepBuilder child) {
        children.add(child);
    }

    @Nonnull
    @Override
    public String getDefaultNameNmPrefix() {
        return "PARENTAREA";
    }

    @Nonnull
    @Override
    public String proposeChildName(StepBuilder child) {
        if (!(child instanceof RowRegionBuilder)) {
            throw new IllegalArgumentException("Only RowStepBuilder can be child of RowParentAreaBuilder");
        }
        int index = children.indexOf(child);
        if (index == -1) {
            throw new IllegalArgumentException("Can only propose internal name for child");
        }
        return child.getDefaultNameNmPrefix() + index;
    }

    private StepBuilder getChildForReference(CellReference cellReference) {
        var cellRow = cellReference.getRow();
        return children.stream().
                filter(child -> ((cellRow >= child.getEffFirstRow().orElseThrow(
                        () -> new RuntimeException(
                                "Cannot evaluate cell path - child effective first row not known " + child))) &&
                        (cellRow <= child.getEffLastRow().orElseThrow(
                                () -> new RuntimeException(
                                        "Cannot evaluate cell path - child effective last row not known " + child))))).
                findFirst().
                orElseThrow(() -> new RuntimeException(
                        "Cannot evaluate cell path - child not found " + this + " for reference " + cellReference));
    }

    @Override
    public void validatePath(StepBuilder fromArea, CellReference cellReference) {
        StepBuilder childForReference = getChildForReference(cellReference);
        if (childForReference.getNameNm().isEmpty()) {
            throw new RuntimeException("Cannot evaluate cell path - child name not known " + childForReference);
        }
        childForReference.validatePath(fromArea, cellReference);
    }

    @Nonnull
    @Override
    public Optional<AreaCellPath> getPath(StepBuilder fromArea, CellReference cellReference) {
        StepBuilder childForReference = getChildForReference(cellReference);
        return childForReference.getPath(fromArea, cellReference).
                map(childPath -> new AreaCellPathRegion(childForReference.getNameNm().orElseThrow(
                        () -> new RuntimeException("Cannot evaluate cell path - child name not known "
                                + childForReference)),
                        childPath));
    }

    @Nonnull
    @Override
    protected RowParentAreaBuilder self() {
        return this;
    }

    private void validateFirstRowOfFirstChild(RowStepBuilder child) {
        if (child.getEffFirstRow().isEmpty()) {
            // first child will inherit start from us
            child.setEffFirstRow(getEffFirstRow().orElseThrow()); // should not fail as we already passed validation...
        } else if (getFirstRow().isPresent()) {
            //noinspection OptionalGetWithoutIsPresent - we verified it is not empty in the first branch of if
            @SuppressWarnings("squid:S3655") // redundant due to check in if
            int childEffFirstRow = child.getEffFirstRow().get();
            int firstRow = getEffFirstRow().orElseThrow(); // we validated eff first row evaluates to value
            if (childEffFirstRow < firstRow) {
                throw new IllegalStateException("Child validity has to be inside parent validity " + child.getNameNm());
            } else if (childEffFirstRow > firstRow) {
                throw new IllegalStateException("Gap between start of region and its first child" + child.getNameNm());
            }
        }
    }

    private void validateFirstRowOfNextChild(RowStepBuilder previousChild, RowStepBuilder child) {
        if (child.getEffFirstRow().isEmpty()) {
            // set child to follow previous child
            child.setEffFirstRow(
                    previousChild.getEffLastRow().
                            orElseThrow(() -> new IllegalStateException("Missing boundary between children "
                                    + previousChild.getNameNm() + " and " + child.getNameNm())) + 1);
        } else {
            @SuppressWarnings("squid:S3655") // redundant due to check in if
            int childEffPrevLastRow = child.getEffFirstRow().get() - 1;
            if (previousChild.getEffLastRow().isEmpty()) {
                // stretch previous child to this
                previousChild.setEffLastRow(childEffPrevLastRow);
            } else {
                @SuppressWarnings("squid:S3655") // redundant due to check in if
                int previousEffLastRow = previousChild.getEffLastRow().get();
                if (previousEffLastRow > childEffPrevLastRow) {
                    throw new IllegalStateException("Children cannot overlap " + previousChild.getNameNm()
                            + " and " + child.getNameNm());
                } else if (previousEffLastRow < childEffPrevLastRow) {
                    throw new IllegalStateException("Gep between " + previousChild.getNameNm()
                            + " and " + child.getNameNm());
                }
            }
        }
    }

    private void validateLastRowOfLastChild(RowStepBuilder lastChild) {
        // verify that last child covers end of this region
        if (lastChild.getEffLastRow().isEmpty()) {
            // stretch last child to end of region
            lastChild.setEffLastRow(getEffLastRow().orElseThrow()); // should not fail as we verified last row in validate
        } else if (getLastRow().isPresent()) {
            //noinspection OptionalGetWithoutIsPresent - was verified by isEmpty in first branch of if
            @SuppressWarnings("squid:S3655")
            int lastChildLastRow = lastChild.getEffLastRow().get();
            int lastRow = getEffLastRow().orElseThrow(); // should not fail as we already passed validation
            if (lastChildLastRow < lastRow) {
                throw new IllegalStateException("Last sub-region must stretch to end of parent");
            }
            if (lastChildLastRow > lastRow) {
                throw new IllegalStateException("Last sub-region must not go beyond end of parent");
            }
        }
    }

    /**
     * Validate sub-regions. Calls validate on each region plus checks that regions are in ascending order of first row
     * and do not overlap.
     */
    private void validateChildren(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        // we go through regions and verify
        // - that they are inside template area of this builder
        // - that they do not overlap
        // If child does not have its last covered row set, we set it right before the first row of next child / last
        // template row of this builder
        // At the end, we validate children themselves
        RowStepBuilder previousChild = null;
        for (RowStepBuilder child : children) {
            if (previousChild == null) {
                validateFirstRowOfFirstChild(child);
            } else {
                validateFirstRowOfNextChild(previousChild, child);
            }
            previousChild = child;
        }
        if (previousChild == null) {
            throw new IllegalStateException("Parent region must have at least one child region");
        }
        validateLastRowOfLastChild(previousChild);
        // and now we can validate children; we couldn't have done it in first pass because end of child might have been
        // filled in on its successor
        for (RowStepBuilder child : children) {
            child.validate(dataSources, template);
        }
    }

    @Override
    protected void afterValidate(Map<String, ReportDataSource> dataSources, TplWorkbook template) {
        validateChildren(dataSources, template);
        super.afterValidate(dataSources, template);
    }

    @Override
    public void validateCellReferences(TplWorkbook template) {
        for (var child : children) {
            child.validateCellReferences(template);
        }
    }

    /**
     * @return collection of sub-regions for this region
     */
    @Nonnull
    private Collection<ReportStep> doBuildChildren(TplWorkbook template) {
        List<ReportStep> builtChildren = new ArrayList<>(children.size());
        for (var child : children) {
            builtChildren.add(child.doBuild(template));
        }
        return builtChildren;
    }

    @Nonnull
    @Override
    public ReportStep doBuild(TplWorkbook template) {
        return new ParentStep(getNameNm().orElseThrow() /*empty should be caught during validation */,
                doBuildChildren(template));
    }

    @Override
    public String toString() {
        return "RowParentAreaBuilder{" +
                "children=" + children +
                ", " + super.toString() +  "}";
    }
}
