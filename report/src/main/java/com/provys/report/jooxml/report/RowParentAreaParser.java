package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

@Singleton
class RowParentAreaParser {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(RowCellAreaParser.class.getName());
    @Nonnull
    private final StepParser stepParser;

    @Inject
    RowParentAreaParser(StepParser stepParser) {
        this.stepParser = Objects.requireNonNull(stepParser);
    }

    RowParentAreaBuilder parse(@Nullable StepBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        RowParentAreaBuilder builder = new RowParentAreaBuilder(parent);
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                StepBuilder child = stepParser.parse(builder, reader);
                if (!(child instanceof RowAreaBuilder)) {
                    throw new RuntimeException("Only row areas allowed in row parent area, not " + name);
                }
                builder.addSubRegion((RowAreaBuilder) child);
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder;
    }

}
