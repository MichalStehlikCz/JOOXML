package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Objects;

/**
 * Reads report body XML file. It acts as parser for root region (treated as parent region with some extensional
 * elements). Unlike StepParsers, it accepts file or input stream and opens XML reader on its own
 */
@ApplicationScoped
class ReportBodyReader {

    private static final Logger LOG = LogManager.getLogger(ReportBodyReader.class.getName());
    private static final String rootElement = "JOOXMLBODY";

    @Nonnull
    private final StepParser stepParser;

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    ReportBodyReader(StepParser stepParser) {
        this.stepParser = Objects.requireNonNull(stepParser);
    }

    private StepBuilder parse(XMLStreamReader reader) throws XMLStreamException {
        var builder = new RootAreaBuilder();
        // read root element
        reader.nextTag();
        if (!reader.getLocalName().equals(rootElement)) {
            throw new RuntimeException("Root elements in JOOXML report body should be " + rootElement + ", not " +
                    reader.getLocalName());
        }
        // read children
        stepParser.parseRowChildren(builder, reader);
        return builder;
    }

    @SuppressWarnings("WeakerAccess")
    StepBuilder read(InputStream reportBodyStream) {
        StepBuilder rootStepBuilder;
        XMLStreamReader stepReader = null;
        try {
            stepReader = XMLInputFactory.newInstance().createXMLStreamReader(reportBodyStream);
            rootStepBuilder = parse(stepReader);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML body", e);
        } finally {
            if (stepReader != null) {
                try {
                    stepReader.close();
                } catch (XMLStreamException e) {
                    LOG.warn("Error closing XML reader for report body");
                }
            }
        }
        return rootStepBuilder;
    }

    StepBuilder read(File reportBodyFile) {
        StepBuilder rootStepBuilder;
        try (var bodyFileStream = new FileInputStream(reportBodyFile)) {
            rootStepBuilder = read(bodyFileStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Parameter file " +  reportBodyFile.getName() + "not found", e);
        } catch (IOException e) {
            throw new RuntimeException("IO exception reading body file", e);
        }
        return rootStepBuilder;
    }

}
