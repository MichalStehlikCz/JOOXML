package com.provys.report.jooxml.datasource;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

interface DataSourceParser {
    DataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException;
}
