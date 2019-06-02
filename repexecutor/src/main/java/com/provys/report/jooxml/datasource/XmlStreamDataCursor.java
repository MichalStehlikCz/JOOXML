package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Receives input stream, reads XML data from this input stream and returns them as data records.
 * On close, close all resources associated with this cursor, including underlying input stream.
 */
class XmlStreamDataCursor extends StreamDataCursorAncestor<XmlFileDataContext> {

    private static final Logger LOG = LogManager.getLogger(XmlStreamDataCursor.class);

    @Nonnull
    private final InputStream inputStream;
    @Nonnull
    private XMLStreamReader reader;

    @SuppressWarnings("squid:S2637") // incorrect warning - Sonar doesn't know that createXMLStreamReader is Nonnull
    XmlStreamDataCursor(XmlFileDataContext dataContext, InputStream inputStream) {
        super(dataContext);
        this.inputStream = Objects.requireNonNull(inputStream);
        try {
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(this.inputStream);
            reader.nextTag();
            if (!reader.getLocalName().equals(this.getDataContext().getDocumentTag())) {
                throw new RuntimeException("Root elements in XML data file should be " +
                        this.getDataContext().getDocumentTag() + ", not " + reader.getLocalName());
            }
            // we have to move from document element... hasNext expects us to be after previous row
            reader.nextTag();
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

    private void processStartElement(int level) {

    }

    /**
     * Skip elements other than our row tag (including content) and find next row element.
     *
     * @return true if row was found, false if end of mster element was reached first
     * @throws XMLStreamException propagated from XML stream reader
     */
    private boolean findRow() throws XMLStreamException {
        int level = 0;
        while (reader.hasNext()) {
            if (reader.isStartElement()) {
                if ((level == 0) && (reader.getLocalName().equals(getDataContext().getRowTag()))) {
                    return true;
                }
                level++;
            } else if (reader.isEndElement()) {
                level--;
                if (level < 0) {
                    // we found end element after all rows...
                    if (!reader.getLocalName().equals(getDataContext().getDocumentTag())) {
                        throw new RuntimeException("End tag " + getDataContext().getDocumentTag() + " expected, " +
                                reader.getLocalName() + " found");
                    }
                    return false;
                }
            }
            reader.nextTag();
        }
        throw new RuntimeException("End of document reached and end tag has not been found");
    }

    /**
     * Skip whitespace and elements other than our designated row element.
     *
     * @return true if row element is found (reader positioned in this element), false if end of parent element is
     * reached before finding row element
     */
    @Override
    public boolean hasNext() {
        try {
            if (!reader.isStartElement() && !reader.isEndElement()) {
                // Transform might end up on newline or similar character - we want to skip whitespace and find next
                // element; if we left it on next section, it would also skip non-whitespace, but we do want to throw
                // exception in such situation
                reader.nextTag();
            }
            // we are on an element (because of nextTag), skip elements other than our row element
            return findRow();
        } catch (XMLStreamException e) {
            throw new RuntimeException("", e);
        }
    }

    @Nonnull
    @Override
    XmlDataRecord getNext(long rowNumber) {
        // both error detection and navigation to next row...
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Document document;
        try {
            TransformerFactory factory = TransformerFactory.newDefaultInstance();
            Transformer transformer = null;
            transformer = factory.newTransformer();
            try (var memOutputStream = new ByteArrayOutputStream()) {
                transformer.transform(new StAXSource(reader), new StreamResult(memOutputStream));
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
                DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                try (var memInputStream = new ByteArrayInputStream(memOutputStream.toByteArray())) {
                    document = builder.parse(memInputStream);
                }
            }
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("", e);
        }
        return new XmlDataRecord(getDataContext().getReportContext(), rowNumber, document);
    }

}
