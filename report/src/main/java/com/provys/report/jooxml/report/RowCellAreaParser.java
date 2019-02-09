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
class RowCellAreaParser {

    private static final String ROW_SPAN_TAG = "ROWS";
    private static final String BIND_TAG = "BIND";
    private static final String ROW_SPAN_REGEXP = "([0-9]+):([0-9]+)";
    private static final Pattern ROW_SPAN_PATTERN = Pattern.compile(ROW_SPAN_REGEXP);

    @Nonnull
    private final CellBindParser cellBindParser;

    @Inject
    RowCellAreaParser(CellBindParser cellBindParser) {
        this.cellBindParser = Objects.requireNonNull(cellBindParser);
    }

    RowCellAreaBuilder parse(@Nullable StepBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        RowCellAreaBuilder builder = new RowCellAreaBuilder(parent);
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case ROW_SPAN_TAG:
                        var matcher = ROW_SPAN_PATTERN.matcher(reader.getElementText());
                        if (!matcher.matches()) {
                            throw new RuntimeException("Row span does not match pattern minrow:maxrow");
                        }
                        if (builder.getFirstRow().isPresent()) {
                            throw new RuntimeException("Duplicate row span specification");
                        }
                        builder.setFirstRow(Integer.valueOf(matcher.group(0)));
                        builder.setLastRow(Integer.valueOf(matcher.group(1)));
                        break;
                    case BIND_TAG:
                        builder.addFieldBind(cellBindParser.parse(reader));
                        break;
                    default:
                        throw new RuntimeException("RowCellArea: Skipping unsupported element " + reader.getLocalName() +
                                "; supported elements are " + ROW_SPAN_TAG + ", " + BIND_TAG);
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder;
    }

}
