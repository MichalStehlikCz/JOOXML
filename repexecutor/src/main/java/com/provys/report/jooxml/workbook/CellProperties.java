package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Used to hold properties associated with cell in workbook. These properties are then assigned to cell in target
 * workbook
 */
public interface CellProperties {

    /**
     * @return index of used style
     */
    @Nonnull
    Optional<Integer> getStyleIndex();

}
