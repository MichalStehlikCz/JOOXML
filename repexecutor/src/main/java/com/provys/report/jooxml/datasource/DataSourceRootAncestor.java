package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Root ancestor for {@code DataSource} implementations.
 * Only implements support for children. Created to support both general data sources and {@code RootDataSource}
 */
public abstract class DataSourceRootAncestor implements ReportDataSource {

    @Nonnull
    private final Map<String, ReportDataSource> children = new ConcurrentHashMap<>(3);

    @Nonnull
    @Override
    public Collection<ReportDataSource> getChildren() {
        return Collections.unmodifiableCollection(children.values());
    }

    /**
     * Add child to data source's child collection.
     *
     * @param child is child data source to be added
     * @throws RuntimeException when child does not have parent or its parent is not this data source
     * @throws RuntimeException when new child has same name as already existing child
     */
    void addChild(ReportDataSource child) {
        if (child.getParent().
                orElseThrow(() -> new RuntimeException("Cannot attach root data source as child")) != this) {
            throw new RuntimeException("Cannot attach child datasource that points to different parent");
        }
        if (children.putIfAbsent(child.getNameNm(), child) != null) {
            throw new RuntimeException(
                    "Cannot attach child - child with this name already exists: " + child.getNameNm());
        }
    }
}
