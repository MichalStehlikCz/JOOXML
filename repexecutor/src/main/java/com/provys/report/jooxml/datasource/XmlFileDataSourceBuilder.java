package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

class XmlFileDataSourceBuilder extends DataSourceAncestorBuilder<XmlFileDataSourceBuilder> {

    @Nullable
    private String fileName;
    @Nullable
    private String documentTag;
    @Nullable
    private String rowTag;

    @Nonnull
    @Override
    XmlFileDataSourceBuilder self() {
        return this;
    }

    @Nonnull
    Optional<String> getFileName() {
        return Optional.ofNullable(fileName);
    }

    public void setFileName(@Nullable String fileName) {
        this.fileName = fileName;
    }

    @Nonnull
    Optional<String> getDocumentTag() {
        return Optional.ofNullable(documentTag);
    }

    public void setDocumentTag(@Nullable String documentTag) {
        this.documentTag = documentTag;
    }

    @Nonnull
    Optional<String> getRowTag() {
        return Optional.ofNullable(rowTag);
    }

    public void setRowTag(@Nullable String rowTag) {
        this.rowTag = rowTag;
    }

    /**
     * Adds validation that select statement is specified
     */
    @Override
    public void validate() {
        super.validate();
        if (fileName == null) {
            throw new RuntimeException("Filename must be specified in " + this);
        }
        if (fileName.isEmpty()) {
            throw new RuntimeException("Filename cannot be empty in " + this);
        }
        if (documentTag == null) {
            throw new RuntimeException("Document tag must be specified in " + this);
        }
        if (documentTag.isEmpty()) {
            throw new RuntimeException("Document tag cannot be empty in " + this);
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
    XmlFileDataSource doCreate(@Nullable ReportDataSource parent) {
        if (parent == null) {
            throw new RuntimeException("Parent of XML data source cannot be null");
        }
        return new XmlFileDataSource(parent, getNameNm().orElseThrow(), getFileName().orElseThrow(),
                getDocumentTag().orElseThrow(), getRowTag().orElseThrow());
    }
}
