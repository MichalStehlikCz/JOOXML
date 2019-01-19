package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Singleton
class CellBindParser {

    private static final Logger LOG = LogManager.getLogger(CellBindParser.class.getName());

    CellBind parse(XMLStreamReader reader) throws XMLStreamException {
        CellBindBuilder builder = new CellBindBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "COLUMN":
                        String column = reader.getElementText();
                        builder.getSourceColumn().filter((val) -> !val.equals(column)).
                                ifPresent((val) -> LOG.warn("Duplicate COLUMN assignment {} > {}", val, column));
                        builder.setSourceColumn(column);
                        break;
                    case "CELL":
                        String address = reader.getElementText();
                        builder.getAddress().filter((val) -> !val.equals(address)).
                                ifPresent((val) -> LOG.warn("Duplicate ADDRESS assignment {} > {}", val, address));
                        builder.setAddress(address);
                        break;
                    default:
                        throw new RuntimeException("RowCellArea: Unsupported element " + reader.getLocalName() +
                                " in BIND; supported elements are CELL, COLUMN");
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder.build();
    }

}
