package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;

import javax.annotation.Nullable;
import java.util.*;

public final class RowParentAreaBuilder extends RowAreaBuilder<RowParentAreaBuilder> {

    final private List<RowAreaBuilder> children = new ArrayList<>(5);

    RowParentAreaBuilder(@Nullable StepBuilder parent) {
        super(parent);
    }

    /**
     * @return subregions as list
     */
    public Collection<RowAreaBuilder> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    @Override
    public String getDefaultNameNmPrefix() {
        return "PARENTAREA";
    }

    @Override
    public String proposeChildName(StepBuilder child) {
        int index = children.indexOf(child);
        if (index == -1) {
            throw new IllegalArgumentException("Can only propose internal name for child");
        }
        return child.getDefaultNameNmPrefix() + index;
    }

    /**
     * Add subregion to list of subregions. Propagate top level flag from this region to subregion.
     */
    public void addSubRegion(RowAreaBuilder child) {
        child.setTopLevel(isTopLevel());
        children.add(child);
    }

    @Override
    protected RowParentAreaBuilder self() {
        return this;
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
                if (child.getFirstRow() == -1) {
                    // first child can inherit start from us
                    child.setFirstRow(getFirstRow());
                } else if (child.getFirstRow() < getFirstRow()) {
                    throw new IllegalStateException("Child validity has to be inside parent validity " + child.getNameNm());
                } else if (child.getFirstRow() > getFirstRow()) {
                    throw new IllegalStateException("Gap between start of region and its first child" + child.getNameNm());
                }
            } else {
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
            throw new IllegalStateException("Last subregion must end at the same row as parent");
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
     * @return collection of subregions for this region
     */
    private Collection<ReportStep> doBuildChildren(TplWorkbook template) {
        List<ReportStep> builtChildren = new ArrayList<>(children.size());
        for (RowAreaBuilder child : children) {
            builtChildren.add(child.doBuild(template));
        }
        return builtChildren;
    }

    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        return new RowParentArea(getNameNm().orElseThrow() /*empty should be caught during validation */,
                doBuildChildren(template));
    }
}
