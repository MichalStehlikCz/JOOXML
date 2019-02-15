package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

final class SelectDataSourceBuilder extends DataSourceAncestorBuilder<SelectDataSourceBuilder> {

    @Nullable
    private String selectStatement = null;

    @Nonnull
    Optional<String> getSelectStatement() {
        return Optional.ofNullable(selectStatement);
    }

    @Nonnull
    SelectDataSourceBuilder setSelectStatement(String selectStatement) {
        this.selectStatement = Objects.requireNonNull(selectStatement);
        return this;
    }

    @Nonnull
    @Override
    SelectDataSourceBuilder self() {
        return this;
    }

    /**
     * Adds validation that select statement is specified
     */
    @Override
    public void validate() {
        super.validate();
        if (getSelectStatement().isEmpty()) {
            throw new RuntimeException("Select statement must be specified");
        }
    }

    @Nonnull
    @Override
    SelectDataSource doCreate(@Nullable ReportDataSource parent) {
        if (parent == null) {
            throw new RuntimeException("Parent of select data source cannot be null");
        }
        return new SelectDataSource(parent, getNameNm().orElseThrow(), getSelectStatement().orElseThrow());
    }
}
