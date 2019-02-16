package com.provys.report.jooxml.datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

class SelectDataSourceParser {
    static final String TAG = "SELECTDATASOURCE";
    private static final String NAME_NM_TAG = "NAME_NM";
    private static final String SELECT_TAG = "SELECT";

    @Nonnull
    private final DataSourceParser dataSourceParser;

    SelectDataSourceParser(DataSourceParser dataSourceParser) {
        this.dataSourceParser = Objects.requireNonNull(dataSourceParser);
    }

    SelectDataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        var builder = new SelectDataSourceBuilder().setParent(parent);
        boolean readNameNm = false;
        boolean readSelect = false;
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
                    case SELECT_TAG:
                        if (readSelect) {
                            throw new RuntimeException("Duplicate tag " + NAME_NM_TAG);
                        }
                        readSelect = true;
                        builder.setSelectStatement(reader.getElementText());
                        break;
                    default:
                        builder.addChild(dataSourceParser.parse(builder, reader));
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder;
    }

}
