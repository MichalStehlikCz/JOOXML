package com.provys.report.jooxml.report;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Singleton
public class RowCellAreaParser {

    @Inject
    private CellBindParser cellBindParser;

    RowCellArea parse(XMLStreamReader reader) throws XMLStreamException {
        RowCellAreaBuilder builder = new RowCellAreaBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("BIND")) {
                    builder.addFieldBind(cellBindParser.parse(reader));
                } else {
                    LOG.error("RowCellArea: Unsupported element {}; supported elements are BIND", reader.getLocalName());
                    throw new RuntimeException("RowCellArea: Skipping unsupported element " + reader.getLocalName() +
                            "; supported elements are BIND");
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return this;
    }

}
