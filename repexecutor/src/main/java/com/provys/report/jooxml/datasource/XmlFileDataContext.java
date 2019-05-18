package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;

import javax.annotation.Nonnull;
import java.io.*;

public class XmlFileDataContext extends DataContextAncestor<XmlFileDataSource> {

    XmlFileDataContext(XmlFileDataSource dataSource, ReportContext reportContext) {
        super(dataSource, reportContext);
    }

    @Override
    public DataCursor execute(DataRecord master) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(getDataSource().getXmlDataFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to open XML data file " + getDataSource().getXmlDataFile(), e);
        }
        return new XmlDataCursor(this, inputStream);
    }

    /**
     * @return document element name from data source
     */
    @Nonnull
    String getDocumentTag() {
        return getDataSource().getDocumentTag();
    }

    /**
     * @return row element name from data source
     */
    @Nonnull
    String getRowTag() {
        return getDataSource().getRowTag();
    }
}
