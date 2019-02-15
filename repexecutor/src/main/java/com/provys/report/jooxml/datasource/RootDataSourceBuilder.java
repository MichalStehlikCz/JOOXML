package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

class RootDataSourceBuilder extends DataSourceRootAncestorBuilder<RootDataSourceBuilder> {

    @Nonnull
    @Override
    public Optional<DataSourceBuilder> getParent() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<String> getNameNm() {
        return Optional.of(RootDataSource.NAME_NM);
    }

    @Nonnull
    @Override
    RootDataSourceBuilder self() {
        return this;
    }

    @Nonnull
    @Override
    RootDataSource doCreate(@Nullable ReportDataSource parent) {
        if (parent != null) {
            throw new RuntimeException("Unexpected parent in create root data source");
        }
        return new RootDataSource();
    }
}
