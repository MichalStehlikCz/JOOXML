package com.provys.report.jooxml.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StepReader implements Closeable {

    private static final Logger LOG = LogManager.getLogger(StepReader.class.getName());
    private final Reader inputReader;
    private XMLStreamReader stepReader;
    private volatile StepBuilder rootStep;

    /**
     * Creates parameter reader from reader. Reader will be kept by ParameterReader and closed when ParameterReader is
     * closed
     *
     * @param inputReader is reader that is source of XML data used to read parameters
     */
    public StepReader(Reader inputReader) {
        this.inputReader = inputReader;
    }

    /**
     * Creates parameter reader from InputSource. InputSource will be used by ParameterReader and closed when
     * ParameterReader is closed
     *
     * @param inputStream is input stream with XML representation of parameters
     */
    public StepReader(InputStream inputStream) {
        this.inputReader = new InputStreamReader(inputStream, UTF_8);
    }

    private static InputStream openFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("ReadSteps: Definition file not found {} {}", file.getName(), e);
            throw new RuntimeException("Definition file not found", e);
        }
    }

    /**
     * Reads region definition from XML file.
     *
     * @param file is file regions should be read from
     * @throws RuntimeException when file is not found
     */
    public StepReader(File file) {
        this(openFile(file));
    }

/*
    private void parseParameters() throws XMLStreamException {
     /
        while (paramReader.hasNext()) {
            int eventType = paramReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = paramReader.getLocalName();
                String value = paramReader.getElementText();
                if (value.isEmpty()) {
                    value = null;
                }
                parameters.add(new Parameter(name, value));
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }


            // PARAMETERS root level tag expected
            int eventType = paramReader.nextTag();
            if (eventType != XMLStreamConstants.START_ELEMENT) {
                LOG.error("ReadParameters: Root start element expected, not {}", eventType);
                throw new RuntimeException("ReadParameters: Root start element expected, not " + eventType);
            }
            if (!paramReader.getLocalName().equals("PARAMETERS")) {
                LOG.error("ReadParameters: Root elements should be PARAMETERS, not {}", paramReader.getLocalName());
                throw new RuntimeException("ReadParameters: Root elements should be PARAMETERS, not "
                        + paramReader.getLocalName());
            }
            parseParameters();
*/

    /**
     * Invoked when pointer is already on start element of step to be read (as caller makes sure that follows step and
     * not end tag)
     *
     * @return created step builder
     */
    private StepBuilder parseStep(@Nullable StepBuilder parent) throws XMLStreamException {
        StepBuilder step;
        switch (stepReader.getLocalName()) {
            case "ROWCELLAREA":
                step = new RowCellAreaBuilder(parent).parse(stepReader);
                break;
            default:
                throw new RuntimeException("ReadSteps: Unsupported step type " + stepReader.getLocalName());
        }
        return step;
    }

    private synchronized void parse() {
        if (rootStep != null) {
            return;
        }
        try {
            stepReader = XMLInputFactory.newInstance().createXMLStreamReader(inputReader);
            int eventType = stepReader.nextTag();
            if (eventType != XMLStreamConstants.START_ELEMENT) {
                LOG.error("ReadSteps: Root start element expected, not {}", eventType);
                throw new RuntimeException("ReadSteps: Root start element expected, not " + eventType);
            }
            rootStep = parseStep(null);
        } catch (XMLStreamException e) {
            LOG.error("ReadSteps: Exception reading XML report definition file {}", e);
            throw new RuntimeException("Exception reading XML report definition file", e);
        } finally {
            try {
                stepReader.close();
            } catch (XMLStreamException e) {
                LOG.warn("ReadSteps: Exception closing XML report definition file {}", e);
            }
        }
        LOG.debug("ReadSteps: Loaded step definition from XML");
    }

    /**
     * Retrieve list of parameters, present in data source this reader has been created on.
     *
     * @return list of parameters and their values
     */
    public StepBuilder getRootStep() {
        if (rootStep == null) {
            parse();
        }
        return rootStep;
    }

    /**
     * Close underlying input source / reader
     *
     * @throws IOException when closing underlaying data source fails
     */
    @Override
    public void close() throws IOException {
        inputReader.close();
    }

}
