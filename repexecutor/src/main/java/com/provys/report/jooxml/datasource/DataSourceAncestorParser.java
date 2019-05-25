package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

abstract class DataSourceAncestorParser<T extends DataSourceAncestorBuilder<T>> implements DataSourceParser {

    private static final Logger LOG = LogManager.getLogger(DataSourceAncestorParser.class);
    private static final String NAME_NM_TAG = "NAME_NM";

    @Nonnull
    private final ChildDataSourceParser childDataSourceParser;

    DataSourceAncestorParser(ChildDataSourceParser childDataSourceParser) {
        this.childDataSourceParser = Objects.requireNonNull(childDataSourceParser);
    }

    void parseElement(XMLStreamReader reader, T builder) throws XMLStreamException {
        String name = reader.getLocalName();
        if (name.equals(NAME_NM_TAG)) {
            if (builder.getNameNm().isPresent()) {
                throw new RuntimeException("Duplicate tag " + NAME_NM_TAG);
            }
            builder.setNameNm(reader.getElementText());
        } else {
            builder.addChild(childDataSourceParser.parse(builder, reader));
        }
    }

    @Nonnull
    abstract T createBuilder(@Nullable DataSourceBuilder parent);

    @Override
    public DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        var builder = createBuilder(parent);
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                parseElement(reader, builder);
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        LOG.debug("Parsed select datasource {}", () -> builder.getNameNm().orElse(null));
        return builder;
    }
}
