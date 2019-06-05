package com.provys.report.jooxml.datasource;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Class supplies parser, that will verify, that XML reader is positioned on empty TEST element and return datasource
 * supplied on instance creation as result
 */
class TestChildDataSourceParser implements DataSourceParser {

    static final String TAG = "TEST";

    private final DataSourceBuilder builder;

    TestChildDataSourceParser(DataSourceBuilder builder) {
        this.builder = builder;
    }

    @Override
    public DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        if ((!reader.isStartElement()) || (!reader.getLocalName().equals(TAG))) {
            throw new RuntimeException("Expected element " + TAG + " in test child reader");
        }
        reader.nextTag();
        if ((!reader.isEndElement()) || (!reader.getLocalName().equals(TAG))) {
            throw new RuntimeException("Expected end element " + TAG + " in test child reader");
        }
        return builder;
    }
}
