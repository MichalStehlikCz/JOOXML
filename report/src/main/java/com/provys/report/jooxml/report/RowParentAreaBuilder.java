package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

final class RowParentAreaBuilder extends RowAreaBuilder<RowParentAreaBuilder> {

    @Nonnull
    private final List<RowAreaBuilder> children = new ArrayList<>(5);

    RowParentAreaBuilder(@Nullable StepBuilder parent) {
        super(parent);
    }

    /**
     * @return subregions as list
     */
    @Nonnull
    public Collection<RowAreaBuilder> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    @Nonnull
    @Override
    public String getDefaultNameNmPrefix() {
        return "PARENTAREA";
    }

    @Nonnull
    @Override
    public String proposeChildName(StepBuilder child) {
        if (!(child instanceof RowAreaBuilder)) {
            throw new IllegalArgumentException("Only RowAreaBuilder can be child of RowParentAreaBuilder");
        }
        int index = children.indexOf(child);
        if (index == -1) {
            throw new IllegalArgumentException("Can only propose internal name for child");
        }
        return child.getDefaultNameNmPrefix() + index;
    }

    /**
     * Add subregion to list of subregions. Propagate top level flag from this region to subregion.
     */
    void addSubRegion(RowAreaBuilder child) {
        child.setTopLevel(isTopLevel());
        children.add(child);
    }

    @Nonnull
    @Override
    protected RowParentAreaBuilder self() {
        return this;
    }

    private void validateFirstRowOfFirstChild(RowAreaBuilder child) {
        if (child.getFirstRow() == -1) {
            // first child can inherit start from us
            child.setFirstRow(getFirstRow());
        } else if (child.getFirstRow() < getFirstRow()) {
            throw new IllegalStateException("Child validity has to be inside parent validity " + child.getNameNm());
        } else if (child.getFirstRow() > getFirstRow()) {
            throw new IllegalStateException("Gap between start of region and its first child" + child.getNameNm());
        }
    }

    private void validateFirstRowOfNextChild(RowAreaBuilder previousChild, RowAreaBuilder child) {
        if (child.getFirstRow() == -1) {
            // set child to follow previous child
            if (previousChild.getLastRow() == -1) {
                throw new IllegalStateException("Missing boundry between children " + previousChild.getNameNm()
                        + " and " + child.getNameNm());
            }
            child.setFirstRow(previousChild.getLastRow() + 1);
        } else {
            if (previousChild.getLastRow() == -1) {
                // stretch previous child to this
                previousChild.setLastRow(child.getFirstRow() - 1);
            } else if (previousChild.getLastRow() > child.getFirstRow() - 1) {
                throw new IllegalStateException("Children cannot overlap " + previousChild.getNameNm()
                        + " and " + child.getNameNm());
            } else if (previousChild.getLastRow() < child.getFirstRow() - 1) {
                throw new IllegalStateException("Gep between " + previousChild.getNameNm()
                        + " and " + child.getNameNm());
            }
        }
    }

    /**
     * Validate sub-regions. Calls validate on each region plus checks that regions are in ascending order of first row
     * and do not overlap.
     */
    private void validateChildren() {
        // first, we must sort children in order of their first covered row; it has to be set, otherwise validation
        // fails
        children.sort(Comparator.comparingInt(RowAreaBuilder::getFirstRow));
        // we go through regions and verify
        // - that they are inside template area of this builder
        // - that they do not overlap
        // If child does not have its last covered row set, we set it right before first row of next child / last
        // template row of this builder
        // At the end, we validate children themselves
        RowAreaBuilder previousChild = null;
        for (RowAreaBuilder child : children) {
            if (previousChild == null) {
                validateFirstRowOfFirstChild(child);
            } else {
                validateFirstRowOfNextChild(previousChild, child);
            }
            if (child.getLastRow() > getLastRow()) {
                throw new IllegalStateException("Child region ends after parent");
            }
            previousChild = child;
        }
        if (previousChild == null) {
            throw new IllegalStateException("Parent region must have at least one child region");
        }
        // verify that last child covers end of this region
        if (previousChild.getLastRow() == -1) {
            // stretch last child to end of region
            previousChild.setLastRow(getLastRow());
        } else if (previousChild.getLastRow() != getLastRow()) {
            throw new IllegalStateException("Last sub-region must end at the same row as parent");
        }
        // and now we can validate children; we couldn't have done it in first pass because end of child might have been
        // filled in on its successor
        for (RowAreaBuilder child : children) {
            child.validate();
        }
    }

    @Override
    protected void validate() {
        super.validate();
        validateChildren();
    }

    /**
     * @return collection of sub-regions for this region
     */
    @Nonnull
    private Collection<ReportStep> doBuildChildren(TplWorkbook template) {
        List<ReportStep> builtChildren = new ArrayList<>(children.size());
        for (RowAreaBuilder child : children) {
            builtChildren.add(child.doBuild(template));
        }
        return builtChildren;
    }

    @Nonnull
    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        return new RowParentArea(getNameNm().orElseThrow() /*empty should be caught during validation */,
                doBuildChildren(template));
    }
}
