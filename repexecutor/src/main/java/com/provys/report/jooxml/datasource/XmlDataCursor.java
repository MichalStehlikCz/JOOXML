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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
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
        try {
            if (!reader.hasNext()) {
                return false;
            }
            if (!reader.isStartElement()) {
                throw new RuntimeException("Element expected in XML source");
            }
            // skip elements other than row element
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
                        return false;
                    }
                }
                reader.nextTag();
            }
        } catch (XMLStreamException e) {
            LOG.error("Exception {}", e);
            throw new RuntimeException("", e);
        }
        return false;
    }

    @Nonnull
    @Override
    DataRecord getNext(long rowNumber) {
        Document document;
        try {
            TransformerFactory factory = TransformerFactory.newDefaultInstance();
            Transformer transformer = null;
            transformer = factory.newTransformer();
            File file = new File("xmlrow.xml");
            transformer.transform(new StAXSource(reader), new StreamResult(file));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            LOG.error("Exception {}", e);
            throw new RuntimeException("", e);
        }
        return new XmlDataRecord(getDataContext().getReportContext(), rowNumber, document);
    }

}
