package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Singleton
class RowCellAreaParser {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(RowCellAreaParser.class.getName());
    @Inject
    private CellBindParser cellBindParser;

    RowCellAreaBuilder parse(StepBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        RowCellAreaBuilder builder = new RowCellAreaBuilder(parent);
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
        return builder;
    }

}
