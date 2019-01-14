package com.provys.report.jooxml.workbook;

import javax.annotation.Nullable;
import java.util.Optional;

public interface CellProperties {

    public static CellProperties getProperties(@Nullable Integer styleIndex) {
        return Workbooks.getProperties(styleIndex);
    }

    Optional<Integer> getStyleIndex();

}
