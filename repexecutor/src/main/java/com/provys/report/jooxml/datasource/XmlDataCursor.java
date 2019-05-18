package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Objects;

/**
 * Receives input stream, reads XML data from this input stream and returns them as data records.
 * On close, close all resources associated with this cursor, including underlying input stream.
 */
class XmlDataCursor extends StreamDataCursorAncestor<XmlFileDataContext> {

    private static final Logger LOG = LogManager.getLogger(XmlDataCursor.class);

    @Nonnull
    private final InputStream inputStream;
    @Nonnull
    private XMLStreamReader reader;

    @SuppressWarnings("squid:S2637") // incorrect warning - Sonar doesn't know that createXMLStreamReader is Nonnull
    XmlDataCursor(XmlFileDataContext dataContext, InputStream inputStream) {
        super(dataContext);
        this.inputStream = Objects.requireNonNull(inputStream);
        try {
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(this.inputStream);
            reader.nextTag();
            if (!reader.getLocalName().equals(this.getDataContext().getDocumentTag())) {
                throw new RuntimeException("Root elements in XML data file should be " +
                        this.getDataContext().getDocumentTag() + ", not " + reader.getLocalName());
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to open xml reader in XML cursor from stream " + this.inputStream, e);
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error closing XML reader " + reader + " in XML cursor", e);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing input stream for XML cursor " + inputStream, e);
        }
    }

    @Override
    public boolean hasNext() {
        
        return false;
    }

    @Nonnull
    @Override
    DataRecord getNext(int rowNumber) {
        return null;
    }

}
