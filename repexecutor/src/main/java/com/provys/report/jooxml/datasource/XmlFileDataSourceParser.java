package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
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
    void parseElement(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        var builder = new XmlFileDataSourceBuilder().setParent(parent);
        boolean readNameNm = false;
        boolean readFileName = false;
        boolean readDocumentTag = false;
        boolean readRowTag = false;
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                switch (name) {
                    case NAME_NM_TAG:
                        if (readNameNm) {
                            throw new RuntimeException("Duplicate tag " + NAME_NM_TAG);
                        }
                        readNameNm = true;
                        builder.setNameNm(reader.getElementText());
                        break;
                    case FILE_NAME_TAG:
                        if (readFileName) {
                            throw new RuntimeException("Duplicate tag " + FILE_NAME_TAG);
                        }
                        readFileName = true;
                        builder.setFileName(reader.getElementText());
                        break;
                    case DOCUMENT_TAG_TAG:
                        if (readDocumentTag) {
                            throw new RuntimeException("Duplicate tag " + DOCUMENT_TAG_TAG);
                        }
                        readDocumentTag = true;
                        builder.setDocumentTag(reader.getElementText());
                        break;
                    case ROW_TAG_TAG:
                        if (readRowTag) {
                            throw new RuntimeException("Duplicate tag " + ROW_TAG_TAG);
                        }
                        readRowTag = true;
                        builder.setRowTag(reader.getElementText());
                        break;
                    default:
                        builder.addChild(childDataSourceParser.parse(builder, reader));
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        LOG.debug("Parsed select datasource {}", () -> builder.getNameNm().orElse(null));
        return builder;
    }

}
