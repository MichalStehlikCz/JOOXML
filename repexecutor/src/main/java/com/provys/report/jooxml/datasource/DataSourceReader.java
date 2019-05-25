package com.provys.report.jooxml.datasource;

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

@ApplicationScoped
public class DataSourceReader {

    private static final Logger LOG = LogManager.getLogger(DataSourceReader.class.getName());
    private static final String ROOT_TAG = "DATASOURCE";

    @Nonnull
    private final ChildDataSourceParser childParser;

    @Inject
    DataSourceReader(ChildDataSourceParser childParser) {
        this.childParser = Objects.requireNonNull(childParser);
    }

    @Nonnull
    private ReportDataSource parse(XMLStreamReader reader) throws XMLStreamException {
        var builder = new RootDataSourceBuilder();
        // read root element
        reader.nextTag();
        if (!reader.getLocalName().equals(ROOT_TAG)) {
            throw new RuntimeException("Root elements in JOOXML data source should be " + ROOT_TAG + ", not " +
                    reader.getLocalName());
        }
        // read content
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                builder.addChild(childParser.parse(builder, reader));
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder.build(null);
    }

    @SuppressWarnings("WeakerAccess")
    public ReportDataSource read(InputStream reportBodyStream) {
        ReportDataSource rootDataSource;
        XMLStreamReader reader = null;
        try {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(reportBodyStream);
            rootDataSource = parse(reader);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading datasource", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    LOG.warn("Error closing XML reader for report body");
                }
            }
        }
        return rootDataSource;
    }

    public ReportDataSource read(File file) {
        ReportDataSource rootDataSource;
        try (var bodyFileStream = new FileInputStream(file)) {
            rootDataSource = read(bodyFileStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Parameter file " +  file.getName() + "not found", e);
        } catch (IOException e) {
            throw new RuntimeException("IO exception reading body file", e);
        }
        return rootDataSource;
    }
}
