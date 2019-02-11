package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Generic ancestor for {@code DataSource} classes. Adds support for parent and internal name to child support of
 * {@code DataSourceRootAncestor}
 */
abstract class DataSourceAncestor extends DataSourceRootAncestor {

    @Nonnull
    private final ReportDataSource parent;
    @Nonnull
    private final String nameNm;

    DataSourceAncestor(ReportDataSource parent, String nameNm) {
        this.parent = Objects.requireNonNull(parent);
        if (nameNm.isEmpty()) {
            throw new IllegalArgumentException("Internal name of ReportDataSource cannot be empty");
        }
        this.nameNm = nameNm;
    }

    @Nonnull
    @Override
    public Optional<ReportDataSource> getParent() {
        return Optional.of(parent);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return nameNm;
    }

}
