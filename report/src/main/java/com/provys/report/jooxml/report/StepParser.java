package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Singleton
public class StepParser {

    private static final Logger LOG = LogManager.getLogger(StepParser.class.getName());

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
        rowCellAreaParser = new RowCellAreaParser(cellBindParser);
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

    /**
     * Invoked when pointer is already on start element of step to be read (as caller makes sure that follows step and
     * not end tag)
     *
     * @return created step builder
     */
    StepBuilder parse(@Nullable StepBuilder parent, XMLStreamReader stepReader) throws XMLStreamException {
        StepBuilder step;
        switch (stepReader.getLocalName()) {
            case "ROWCELLAREA":
                step = rowCellAreaParser.parse(parent, stepReader);
                break;
            case "ROWPARENTAREA":
                step = rowParentAreaParser.parse(parent, stepReader);
                break;
            case "ROWREPEATER":
                step = rowRepeaterParser.parse(parent, stepReader);
                break;
            default:
                throw new RuntimeException("ReadSteps: Unsupported step type " + stepReader.getLocalName());
        }
        return step;
    }

}
