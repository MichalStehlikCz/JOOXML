package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Adds handling of parent and internal name to {@code DataSourceRootAncestorBuilder}
 */
abstract class DataSourceAncestorBuilder<T extends DataSourceAncestorBuilder> extends DataSourceRootAncestorBuilder<T> {

    @Nullable
    private DataSourceBuilder parent;
    @Nullable
    private String nameNm;

    @Nonnull
    @Override
    public Optional<DataSourceBuilder> getParent() {
        return Optional.ofNullable(parent);
    }

    @Nonnull
    public T setParent(@Nullable DataSourceBuilder parent) {
        this.parent = parent;
        return self();
    }

    @Nonnull
    @Override
    public Optional<String> getNameNm() {
        return Optional.ofNullable(nameNm);
    }

    @Nonnull
    public T setNameNm(String nameNm) {
        this.nameNm = Objects.requireNonNull(nameNm);
        return self();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "nameNm='" + nameNm + '\'' +
                "}";
    }
}
