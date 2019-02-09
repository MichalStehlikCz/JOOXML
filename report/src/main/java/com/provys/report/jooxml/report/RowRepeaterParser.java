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

    private static final String ROWS_TAG = "ROWS";
    private static final String BODY_TAG = "BODY";
    private static final String DATASOURCE_TAG = "DATASOURCE";
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
                    case ROWS_TAG:
                        var rowsMatcher = ROW_SPAN_PATTERN.matcher(reader.getElementText());
                        if (!rowsMatcher.matches()) {
                            throw new RuntimeException("Row span does not match pattern minrow:maxrow");
                        }
                        if (builder.getFirstRow().isPresent()) {
                            throw new RuntimeException("Duplicate row span specification");
                        }
                        builder.setFirstRow(Integer.valueOf(rowsMatcher.group(0)));
                        builder.setLastRow(Integer.valueOf(rowsMatcher.group(1)));
                        break;
                    case BODY_TAG:
                        var bodyMatcher = ROW_SPAN_PATTERN.matcher(reader.getElementText());
                        if (!bodyMatcher.matches()) {
                            throw new RuntimeException("Row span does not match pattern minrow:maxrow");
                        }
                        if (builder.getFirstBodyRow().isPresent()) {
                            throw new RuntimeException("Duplicate body span specification");
                        }
                        builder.setFirstBodyRow(Integer.valueOf(bodyMatcher.group(0)));
                        builder.setLastBodyRow(Integer.valueOf(bodyMatcher.group(1)));
                        break;
                    case DATASOURCE_TAG:
                        if (builder.getDataSourceName().isPresent()) {
                            throw new RuntimeException("Duplicate datasource specification");
                        }
                        builder.setDataSourceName(reader.getElementText());
                        break;
                    default:
                        StepBuilder child = stepParser.parse(builder, reader);
                        if (!(child instanceof RowRegionBuilder)) {
                            throw new RuntimeException("Only row areas allowed in row parent area, not " + name);
                        }
                        if (builder.getChild().isPresent()) {
                            throw new RuntimeException("Only one child region allowed in RowRepeater");
                        }
                        builder.setChild((RowStepBuilder) child);
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder;
    }

}
