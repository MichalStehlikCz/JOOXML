package com.provys.report.jooxml.repexecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Enables addressing of cell in template describing path leading to this cell defined as references to names of
 * report steps, starting from root and ending in cell area region containing given cell plus address of cell relative
 * to given area. In case of repeaters, it defines line number relative to current line (0 is default, pointing to
 * current line, but it is also possible to use absolute addressing e.g. to first / last line of repeater).
 */
public interface AreaCellPath {

    /**
     * Retrieve path (absolute - with resolved references to lines in repeater areas), corresponding to given area cell
     * path in context of data addressed by execution region being currently processed. It assumes that area path is
     * valid for given context (e.g. in case of repeaters, it can only use absolute addressing for repeaters not being
     * on the stack). It returns empty optional if resulting address is invalid (e.g. in case relative addressing is
     * used in repeater and resulting line number would be negative). On the other hand it is expected that evaluation
     * is done during evaluation of area containing cell with reference and thus it is not possible to validate line
     * numbers for areas that are ahead of current region - these line numbers are evaluated as result of arithmetic
     * expression and not validated against region.
     *
     * @param execRegionPath is region being processed; lines in repeater records with relative position are
     *                         evaluated against this region. It might be unspecified if reference goes to subregions of
     *                       current region or if on one of higher level repeaters, reference has gone to other than
     *                       current line (absolute record reference or relative record reference with offset other than
     *                       0) or path has gone to different region
     * @return absolute path corresponding to this cell and current region
     * @throws RuntimeException if there is relative line number that does not correspond to repeater in current
     * path
     */
    @Nonnull
    Optional<CellPath> getCellPath(@Nullable ExecRegionPath execRegionPath);
}

