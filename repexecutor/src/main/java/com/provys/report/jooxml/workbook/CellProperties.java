package com.provys.report.jooxml.workbook;

import java.util.Optional;

public interface CellProperties {
    int getStyleIndex();
    Optional<Comment> getComment();
    Optional<Hyperlink> getHyperlink();
}
