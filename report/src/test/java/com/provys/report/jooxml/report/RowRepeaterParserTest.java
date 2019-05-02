package com.provys.report.jooxml.report;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RowRepeaterParserTest {

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(new Object[]{"<ROWREPEATER><BODY>2:4</BODY></ROWREPEATER>",
                        null, null, null, 1, 3, Boolean.FALSE}
                , new Object[]{"<ROWREPEATER><ROWS>1:6</ROWS></ROWREPEATER>",
                        null, 0, 5, null, null, Boolean.FALSE}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String testData, @Nullable String expNameNm, @Nullable Integer expFirstRow,
                   @Nullable Integer expLastRow, @Nullable Integer expFirstBodyRow, @Nullable Integer expLastBodyRow,
                   Boolean shouldFail) {
        var stepParser = mock(StepParser.class);
        var parent = mock(StepBuilder.class);
        var parser = new RowRepeaterParser(stepParser);
        try {
            var xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(testData));
            xmlReader.nextTag();
            if (shouldFail) {
                assertThatThrownBy(() -> parser.parse(parent, xmlReader));
            } else {
                var builder = parser.parse(parent, xmlReader);
                assertThat(builder.getParent().orElse(null)).isEqualTo(parent);
                assertThat(builder.getNameNm().orElse(null)).isEqualTo(expNameNm);
                assertThat(builder.getFirstRow().orElse(null)).isEqualTo(expFirstRow);
                assertThat(builder.getLastRow().orElse(null)).isEqualTo(expLastRow);
                assertThat(builder.getFirstBodyRow().orElse(null)).isEqualTo(expFirstBodyRow);
                assertThat(builder.getLastBodyRow().orElse(null)).isEqualTo(expLastBodyRow);
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("Test failed");
        }
    }
}