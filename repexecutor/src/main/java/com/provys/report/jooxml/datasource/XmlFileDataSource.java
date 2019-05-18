package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;

class XmlFileDataSource extends DataSourceAncestor {

    @Nonnull
    private final File xmlDataFile;
    @Nonnull
    private final String documentTag;
    @Nonnull
    private final String rowTag;

    XmlFileDataSource(ReportDataSource parent, String nameNm, String fileName, String documentTag, String rowTag) {
        super(parent, nameNm);
        this.xmlDataFile = new File(fileName);
        this.documentTag = Objects.requireNonNull(documentTag);
        this.rowTag = Objects.requireNonNull(rowTag);
    }

    /**
     * @return value of field xmlDataFile
     */
    @Nonnull
    File getXmlDataFile() {
        return xmlDataFile;
    }

    /**
     * @return value of field documentTag
     */
    @Nonnull
    public String getDocumentTag() {
        return documentTag;
    }

    /**
     * @return value of field rowTag
     */
    @Nonnull
    public String getRowTag() {
        return rowTag;
    }

    @Nonnull
    @Override
    public DataContext getDataContext(ReportContext reportContext) {
        return new XmlFileDataContext(this, reportContext);
    }
}
