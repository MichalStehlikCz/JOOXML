package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ChildDataSourceParser {

    private static final Logger LOG = LogManager.getLogger(ChildDataSourceParser.class.getName());

    @Nonnull
    private final Map<String, DataSourceParser> parsers;

    /**
     * Constructor that should be used by dependency injection.
     * Various specialized parsers are initialized within constructor, parameters needed for their initialisation are
     * supplied and can be injected
     */
    @Inject
    ChildDataSourceParser() {
        parsers = new HashMap<>(3);
        parsers.put(SelectDataSourceParser.TAG, new SelectDataSourceParser(this));
        parsers.put(XmlFileDataSourceParser.TAG, new XmlFileDataSourceParser(this));
    }

    /**
     * Constructor variant with supplied parsers.
     * This variant is not used for injection (as some parsers depend on StepParser instance) but might be useful for
     * testing purposes
     */
    ChildDataSourceParser(Map<String, DataSourceParser> parsers) {
        this.parsers = new HashMap<>(parsers);
    }

    /**
     * Invoked when pointer is already on start element of datasource to be read (as caller makes sure that follows step and
     * not end tag)
     *
     * @return created step builder
     */
    DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        DataSourceParser parser = parsers.get(reader.getLocalName());
        if (parser == null) {
            throw new RuntimeException("ReadSteps: Unsupported data source type " + reader.getLocalName());
        }
        return parser.parse(parent, reader);
    }
}
