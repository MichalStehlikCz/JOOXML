package com.provys.report.jooxml.workbook;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface RowProperties {

    /**
     * @return row height in points, empty if row does not have explicitly set height
     */
    @Nonnull
    Optional<Float> getHeightInPoints();

    /**
     * @return if row is hidden
     */
    boolean isHidden();

    /**
     * @return row cell style index; we do not manipulate with styles, thus it is possible to transfer style from
     * template to target sheet using index
     */
    int getStyleIndex();
}
