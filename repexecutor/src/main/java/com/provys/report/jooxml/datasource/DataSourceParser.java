package com.provys.report.jooxml.datasource;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Parser used to parse any data source out of XML reader.
 */
interface DataSourceParser {
    /**
     * Extract data source }in form of builder] out of XML reader
     *
     * @param parent - data source builder that will be used as parent for extracted data source
     * @param reader / XML reader, positioned on opening element of data source definition
     * @return extracted data source builder
     * @throws XMLStreamException when anz XML stream exception is encountered reading supplied XML reader
     */
    DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException;
}
