package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@ApplicationScoped
public class DataSourceParser {

    private static final Logger LOG = LogManager.getLogger(DataSourceParser.class.getName());

    @Nonnull
    private final SelectDataSourceParser selectDataSourceParser;

    /**
     * Constructor that should be used by dependency injection.
     * Various specialized parsers are initialized within constructor, parameters needed for their initialisation are
     * supplied and can be injected
     */
    @Inject
    DataSourceParser() {
        selectDataSourceParser = new SelectDataSourceParser(this);
    }

    /**
     * Constructor variant with supplied parsers for individual datasource types.
     * This variant is not used for injection (as some parsers depend on StepParser instance) but might be useful for
     * testing purposes
     *
     * @param selectDataSourceParser is parser to be used for SELECTDATASOURCE steps
     */
    DataSourceParser(SelectDataSourceParser selectDataSourceParser) {
        this.selectDataSourceParser = selectDataSourceParser;
    }

    /**
     * Invoked when pointer is already on start element of datasource to be read (as caller makes sure that follows step and
     * not end tag)
     *
     * @return created step builder
     */
    DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        DataSourceBuilder builder;
        switch (reader.getLocalName()) {
            case SelectDataSourceParser.TAG:
                builder = selectDataSourceParser.parse(parent, reader);
                break;
            default:
                throw new RuntimeException("ReadSteps: Unsupported data source type " + reader.getLocalName());
        }
        return builder;
    }
}
