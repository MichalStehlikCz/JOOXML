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

    private void parseParameter() {

    }

    private synchronized void parse() {
        if (parameters != null) {
            return;
        }
        parameters = new ArrayList<> ();
        XMLStreamReader paramReader;
        try {
            paramReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            List<Parameter> result = new ArrayList<>(10);
            boolean rootRead = false;
            while (paramReader.hasNext()) {
                int eventType = paramReader.next();
                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    if (!rootRead) {
                        if (paramReader.getLocalName() != "PARAMETERS") {
                            LOG.error("ReadParameters: Root elements should be PARAMETERS, not {}"
                                    , paramReader.getLocalName());
                            throw new RuntimeException("ReadParameters: Root elements should be PARAMETERS, not "
                                    + paramReader.getLocalName());
                        }
                    } else {

                    }
                }
            }

        } catch (
                XMLStreamException e) {
            LOG.error("ReadParameters: Exception reading XML parameter file {}", e);
            throw new RuntimeException("Exception reading XML parameter file", e);
        }
    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parse();
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
