package com.provys.report.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ParameterReader implements Closeable {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private final Reader inputReader;
    private XMLStreamReader paramReader;
    private volatile List<Parameter> parameters;

    /**
     * Creates parameter reader from reader. Reader will be kept by ParameterReader and closed when ParameterReader is
     * closed
     *
     * @param inputReader is reader that is source of XML data used to read parameters
     */
    public ParameterReader(Reader inputReader) {
        this.inputReader = inputReader;
    }

    /**
     * Creates parameter reader from InputSource. InputSource will be used by ParameterReader and closed when
     * ParameterReader is closed
     *
     * @param inputStream is input stream with XML representation of parameters
     */
    public ParameterReader(InputStream inputStream) {
        this.inputReader = new InputStreamReader(inputStream, UTF_8);
    }

    private static InputStream openFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("ReadParameters: Parameter file not found {} {}", file.getName(), e);
            throw new RuntimeException("Parameter file not found", e);
        }
    }

    /**
     * Reads parameters from XML file.
     *
     * @param file is file parameters should be read from
     * @throws RuntimeException when file is not found
     */
    public ParameterReader(File file) {
        this(openFile(file));
    }

    private void parseParameters() throws XMLStreamException {
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

    private synchronized void parse() {
        if (parameters != null) {
            return;
        }
        parameters = new ArrayList<>(10);
        try {
            paramReader = XMLInputFactory.newInstance().createXMLStreamReader(inputReader);
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
        } catch (XMLStreamException e) {
            LOG.error("ReadParameters: Exception reading XML parameter file {}", e);
            throw new RuntimeException("Exception reading XML parameter file", e);
        } finally {
            try {
                paramReader.close();
            } catch (XMLStreamException e) {
                LOG.warn("ReadParameters: Exception closing XML parameter file {}", e);
            }
        }
    }

    /**
     * Retrieve list of parameters, present in data source this reader has been created on.
     *
     * @return list of parameters and their values
     */
    public List<Parameter> getParameters() {
        if (parameters == null) {
            parse();
        }
        return parameters;
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
