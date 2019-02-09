package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

abstract class DataSourceAncestor implements ReportDataSource {

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
