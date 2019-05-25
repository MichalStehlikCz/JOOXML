package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class XmlFileDataSourceParser extends DataSourceAncestorParser<XmlFileDataSourceBuilder> {

    private static final Logger LOG = LogManager.getLogger(com.provys.report.jooxml.datasource.SelectDataSourceParser.class);
    static final String TAG = "XMLFILEDATASOURCE";
    private static final String FILE_NAME_TAG = "FILENAME";
    private static final String DOCUMENT_TAG_TAG = "ROOT";
    private static final String ROW_TAG_TAG = "ROW";

    XmlFileDataSourceParser(ChildDataSourceParser childDataSourceParser) {
        super(childDataSourceParser);
    }

    @Override
    void parseElement(XMLStreamReader reader, XmlFileDataSourceBuilder builder) throws XMLStreamException {
        switch (reader.getLocalName()) {
            case FILE_NAME_TAG:
                if (builder.getFileName().isPresent()) {
                    throw new RuntimeException("Duplicate tag " + FILE_NAME_TAG);
                }
                builder.setFileName(reader.getElementText());
                break;
            case DOCUMENT_TAG_TAG:
                if (builder.getDocumentTag().isPresent()) {
                    throw new RuntimeException("Duplicate tag " + DOCUMENT_TAG_TAG);
                }
                builder.setDocumentTag(reader.getElementText());
                break;
            case ROW_TAG_TAG:
                if (builder.getRowTag().isPresent()) {
                    throw new RuntimeException("Duplicate tag " + ROW_TAG_TAG);
                }
                builder.setRowTag(reader.getElementText());
                break;
            default:
                super.parseElement(reader, builder);
        }
    }

    @Nonnull
    @Override
    XmlFileDataSourceBuilder createBuilder(@Nullable DataSourceBuilder parent) {
        return new XmlFileDataSourceBuilder().setParent(parent);
    }

}
