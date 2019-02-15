package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Ancestor for data source builders, including root builder.
 * Implements handling and validation of children
 */
abstract class DataSourceRootAncestorBuilder<U extends DataSourceRootAncestorBuilder> implements DataSourceBuilder {

    @Nonnull
    private final Collection<DataSourceBuilder> children = new ArrayList<>(3);

    @Nonnull
    public Collection<DataSourceBuilder> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Add child to data source's child collection.
     *
     * @param child is child data source to be added
     * @throws RuntimeException when child does not have parent or its parent is not this data source
     */
    U addChild(DataSourceBuilder child) {
        children.add(child);
        return self();
    }

    @Nonnull
    abstract U self();

    /**
     * Validates all children, validates that internal name is not empty and that all children have nonempty internal
     * names
     */
    @Override
    public void validate() {
        if (getNameNm().isEmpty()) {
            throw new RuntimeException("Validation failed - internal name is not specified");
        }
        var childNameNm = new TreeSet<String>();
        for (var child : children) {
            if (child.getParent().
                    orElseThrow(() -> new RuntimeException("Cannot use root data source builder as child")) != this) {
                throw new RuntimeException("Child data-source builder cannot point to different parent");
            }
            child.validate();
            if (!childNameNm.add(child.getNameNm().
                    orElseThrow())) { // should not throw as null should be caught by child validation
                throw new RuntimeException("Validation failed - children have duplicate internal names");
            }
        }
    }

    /**
     * Builds children and adds them to created data source
     *
     * @param dataSource is {@code ReportDataSource} created from this builder
     */
    private void doBuildChildren(DataSourceRootAncestor dataSource) {
        for (var child : children) {
            dataSource.addChild(child.doBuild(dataSource));
        }
    }

    @Nonnull
    abstract DataSourceRootAncestor doCreate(@Nullable ReportDataSource parent);

    @Nonnull
    @Override
    public DataSourceRootAncestor doBuild(@Nullable ReportDataSource parent) {
        var dataSource = doCreate(parent);
        doBuildChildren(dataSource);
        return dataSource;
    }

    /**
     * Build invokes validation and then calls {@code doBuild}
     */
    @Nonnull
    @Override
    public DataSourceRootAncestor build(@Nullable ReportDataSource parent) {
        validate();
        return doBuild(parent);
    }
}
