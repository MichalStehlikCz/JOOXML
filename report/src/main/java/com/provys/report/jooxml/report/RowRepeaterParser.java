package com.provys.report.jooxml.report;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;
import java.util.regex.Pattern;

@ApplicationScoped
public class RowRepeaterParser {

    private static final String ROW_SPAN_TAG = "ROWSPAN";
    private static final String BIND_TAG = "BIND";
    private static final String ROW_SPAN_REGEXP = "([0-9]+):([0-9]+)";
    private static final Pattern ROW_SPAN_PATTERN = Pattern.compile(ROW_SPAN_REGEXP);

    @Nonnull
    private final StepParser stepParser;

    @Inject
    RowRepeaterParser(StepParser stepParser) {
        this.stepParser = Objects.requireNonNull(stepParser);
    }

    RowRepeaterBuilder parse(@Nullable StepBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        var builder = new RowRepeaterBuilder(parent);
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                switch (name) {
                    default:
                        StepBuilder child = stepParser.parse(builder, reader);
                        if (!(child instanceof RowRegionBuilder)) {
                            throw new RuntimeException("Only row areas allowed in row parent area, not " + name);
                        }
                        if (builder.getChild().isPresent())
                        builder.setChild((RowStepBuilder) child);
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder;
    }

}
