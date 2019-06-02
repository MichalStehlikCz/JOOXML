package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class XmlChildDataSourceBuilder extends DataSourceAncestorBuilder<XmlChildDataSourceBuilder> {

    @Nullable
    private String masterTag;
    @Nullable
    private String rowTag;

    @Nonnull
    @Override
    XmlChildDataSourceBuilder self() {
        return this;
    }

    @Nonnull
    Optional<String> getMasterTag() {
        return Optional.ofNullable(masterTag);
    }

    public void setMasterTag(@Nullable String masterTag) {
        this.masterTag = masterTag;
    }

    @Nonnull
    Optional<String> getRowTag() {
        return Optional.ofNullable(rowTag);
    }

    public void setRowTag(@Nullable String rowTag) {
        this.rowTag = rowTag;
    }

    /**
     * Adds validation that master and row tags are specified
     */
    @Override
    public void validate() {
        super.validate();
        if (masterTag == null) {
            throw new RuntimeException("Master tag must be specified in " + this);
        }
        if (masterTag.isEmpty()) {
            throw new RuntimeException("Master tag cannot be empty in " + this);
        }
        if (rowTag == null) {
            throw new RuntimeException("Row tag must be specified in " + this);
        }
        if (rowTag.isEmpty()) {
            throw new RuntimeException("Row tag cannot be empty in " + this);
        }
    }

    @Nonnull
    @Override
    XmlChildDataSource doCreate(@Nullable ReportDataSource parent) {
        if (parent == null) {
            throw new RuntimeException("Parent of XML data source cannot be null");
        }
        return new XmlChildDataSource(parent, getNameNm().orElseThrow(), getMasterTag().orElseThrow(),
                getRowTag().orElseThrow());
    }
}
