package com.provys.report.jooxml.report;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@ApplicationScoped
public class StepParser {

    static final String NAME_NM_TAG = "NAME_NM";

    @Nonnull
    private final RowCellAreaParser rowCellAreaParser;
    @Nonnull
    private final RowParentAreaParser rowParentAreaParser;
    @Nonnull
    private final RowRepeaterParser rowRepeaterParser;

    /**
     * Constructor that should be used by dependency injection.
     * Various specialized parsers are initialized within constructor, parameters needed for their initialisation are
     * supplied and can be injected
     *
     * @param cellBindParser is parser for reading cell binds from XML
     */
    @Inject
    StepParser(CellBindParser cellBindParser) {
        rowCellAreaParser = new RowCellAreaParser(this, cellBindParser);
        rowParentAreaParser = new RowParentAreaParser(this);
        rowRepeaterParser = new RowRepeaterParser(this);
    }

    /**
     * Constructor variant with supplied parsers for individual cell types.
     * This variant is not used for injection (as some parsers depend on StepParser instance) but might be useful for
     * testing purposes
     *
     * @param rowCellAreaParser is parser to be used for ROWCELLAREA steps
     * @param rowParentAreaParser is parser to be used for ROWPARENTAREA steps
     */
    StepParser(RowCellAreaParser rowCellAreaParser, RowParentAreaParser rowParentAreaParser,
               RowRepeaterParser rowRepeaterParser) {
        this.rowCellAreaParser = rowCellAreaParser;
        this.rowParentAreaParser = rowParentAreaParser;
        this.rowRepeaterParser = rowRepeaterParser;
    }

    void parseNameNm(StepBuilderBase builder, XMLStreamReader reader) throws XMLStreamException {
        if (builder.getNameNm().isPresent()) {
            throw new RuntimeException("Duplicate internal name specification " +
                    reader.getElementText());
        }
        builder.setNameNm(reader.getElementText());
    }

    void parseRowChildren(RowParentAreaBuilder builder, XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                if (name.equals(NAME_NM_TAG)) {
                    parseNameNm(builder, reader);
                } else {
                    StepBuilder child = parse(builder, reader);
                    if (!(child instanceof RowRegionBuilder)) {
                        throw new RuntimeException("Only row areas allowed in row parent area, not " + name);
                    }
                    builder.addChild((RowStepBuilder) child);
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    /**
     * Invoked when pointer is already on start element of step to be read (as caller makes sure that follows step and
     * not end tag)
     *
     * @return created step builder
     */
    StepBuilder parse(@Nullable StepBuilder parent, XMLStreamReader stepReader) throws XMLStreamException {
        StepBuilder step;
        switch (stepReader.getLocalName()) {
            case RowCellAreaParser.TAG:
                step = rowCellAreaParser.parse(parent, stepReader);
                break;
            case RowParentAreaParser.TAG:
                step = rowParentAreaParser.parse(parent, stepReader);
                break;
            case RowRepeaterParser.TAG:
                step = rowRepeaterParser.parse(parent, stepReader);
                break;
            default:
                throw new RuntimeException("ReadSteps: Unsupported step type " + stepReader.getLocalName());
        }
        return step;
    }

}
