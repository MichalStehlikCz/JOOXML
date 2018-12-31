package com.provys.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParameterReader implements Closeable {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private final InputStream inputStream;
    private XMLStreamReader paramReader;
    private volatile List<Parameter> parameters;

    public ParameterReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ParameterReader(File file) {
        try {
            this.inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("ReadParameters: Parameter file not found {} {}", file.getName(), e);
            throw new RuntimeException("Parameter file not found", e);
        }
    }

    private void parseParameters() throws XMLStreamException {
        while (paramReader.hasNext()) {
            int eventType = paramReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                Parameter parameter = new Parameter(paramReader.getLocalName(), paramReader.getElementText());
                parameters.add(parameter);
                eventType = paramReader.nextTag();
                if (eventType != XMLStreamConstants.END_ELEMENT) {
                    LOG.error("ReadParameters: End element expected for parameter {}, event type {} found"
                            , parameter.getName(), eventType);
                    throw new RuntimeException("ReadParameters: End element expected for parameter "
                            + parameter.getName() + ", event type " + eventType + " found");
                }
                if (!paramReader.getLocalName().equals(parameter.getName())) {
                    LOG.error("ReadParameters: End element expected for parameter {}, {} found", parameter.getName()
                            , paramReader.getLocalName());
                    throw new RuntimeException("ReadParameters: End element expected for parameter "
                            + parameter.getName() + ", " + paramReader.getLocalName() + " found");
                }
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
            paramReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            // PARAMETERS root level tag expected
            int eventType = paramReader.nextTag();
            if ((eventType != XMLStreamConstants.START_ELEMENT) || (!paramReader.getLocalName().equals("PARAMETERS"))) {
                LOG.error("ReadParameters: Root elements should be PARAMETERS, not {}"
                        , paramReader.getLocalName());
                throw new RuntimeException("ReadParameters: Root elements should be PARAMETERS, not "
                        + paramReader.getLocalName());
            }
            parseParameters();
            if ((eventType != XMLStreamConstants.END_ELEMENT) || (!paramReader.getLocalName().equals("PARAMETERS"))) {
                LOG.error("ReadParameters: End PARAMETERS elements expected, not {}", paramReader.getLocalName());
                throw new RuntimeException("ReadParameters: Root PARAMETERS elements expected, not "
                        + paramReader.getLocalName());
            }
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

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parse();
        }
        return parameters;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
