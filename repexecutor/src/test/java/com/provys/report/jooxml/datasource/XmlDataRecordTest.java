package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class XmlDataRecordTest {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newDefaultInstance();

    @Nonnull
    static Stream<Object[]> getValueTest() {
        return Stream.of(
                new Object[]{"<DATA><COLUMN1>value1</COLUMN1></DATA>", "COLUMN1", "value1"}
                , new Object[]{"<DATA><COLUMN1>value1</COLUMN1></DATA>", "COLUMN2", null}
                , new Object[]{"<DATA attr1=\"attr value 1\"><COLUMN1>value1</COLUMN1></DATA>", "@attr1", "attr value 1"}
        );
    }


    @ParameterizedTest
    @MethodSource
    void getValueTest(String xmlData, String columnName, @Nullable Object expValue) {
        Document document;
        try {
            var builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            try (var stringReader = new StringReader(xmlData)) {
                document = builder.parse(new InputSource(stringReader));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        var reportContext = mock(ReportContext.class);
        var record = new XmlDataRecord(reportContext, 0, document);
        assertThat(record.getValue(columnName, null)).isEqualTo(Optional.ofNullable(expValue));
    }
}