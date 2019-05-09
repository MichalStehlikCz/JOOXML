package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Class keeps pointer to current execution region and parent execution regions.
 * While we have ExecRegion structure that allows to keep map of regions that has been exported to excel, during cell
 * processing, we also need to be able to evaluate path to current cell. We do not want to add parent pointer to
 * execution regions as they are retained for whole report execution... thus we use ExecRegionContext for this purpose
 */
public class ExecRegionContext {
    @Nullable
    private final ExecRegionContext parent;
    @Nonnull
    private final ExecRegion execRegion;

    public ExecRegionContext(@Nullable ExecRegionContext parent, ExecRegion execRegion) {
        this.parent = parent;
        this.execRegion = Objects.requireNonNull(execRegion);
    }

    @Nonnull
    public Optional<ExecRegionContext> getParent() {
        return Optional.ofNullable(parent);
    }

    @Nonnull
    public ExecRegion getExecRegion() {
        return execRegion;
    }

    /**
     * Add new area execution region under this region context. See {@link ExecRegion#addArea}
     *
     * @param nameNm is internal name of area to be added
     * @param coordinates represent upper left corner of area where content of this area will be placed (and thus index
     *                   that has to be used for any coordinate within given area)
     * @return reference to child area context
     */
    @Nonnull
    public ExecRegionContext addArea(String nameNm, ContextCoordinates coordinates) {
        return new ExecRegionContext(this, getExecRegion().addArea(nameNm, coordinates));
    }

    /**
     * Add new non-leaf level region under this region. See {@link ExecRegion#addRegion}
     *
     * @param nameNm is internal name of subregion being inserted
     * @param subRegions is expected number of subregions (to pre-allocate space)
     * @return reference to child region context
     */
    @Nonnull
    public ExecRegionContext addRegion(String nameNm, int subRegions) {
        return new ExecRegionContext(this, getExecRegion().addRegion(nameNm, subRegions));
    }

    /**
     * Add new table region under this region. See {@link ExecRegion#addTable}.
     *
     * @param nameNm is internal name of new child table region
     * @return reference to new child region
     */
    @Nonnull
    public ExecRegionContext addTable(String nameNm) {
        return new ExecRegionContext(this, getExecRegion().addTable(nameNm));
    }
}
