package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class XmlChildDataSourceParser  extends DataSourceAncestorParser<XmlChildDataSourceBuilder> {

    private static final Logger LOG = LogManager.getLogger(XmlChildDataSourceParser.class);
    static final String TAG = "XMLCHILDDATASOURCE";
    private static final String MASTER_TAG_TAG = "MASTER";
    private static final String ROW_TAG_TAG = "ROW";

    XmlChildDataSourceParser(ChildDataSourceParser childDataSourceParser) {
        super(childDataSourceParser);
    }

    @Override
    void parseElement(XMLStreamReader reader, XmlChildDataSourceBuilder builder) throws XMLStreamException {
        switch (reader.getLocalName()) {
            case MASTER_TAG_TAG:
                if (builder.getMasterTag().isPresent()) {
                    throw new RuntimeException("Duplicate tag " + MASTER_TAG_TAG);
                }
                builder.setMasterTag(reader.getElementText());
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
    XmlChildDataSourceBuilder createBuilder(@Nullable DataSourceBuilder parent) {
        return new XmlChildDataSourceBuilder().setParent(parent);
    }

}
