package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.CellCoordinatesFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CellBindParserTest {

    private static CellCoordinatesFactory cellCoordinatesFactory = mock(CellCoordinatesFactory.class);
    private static CellCoordinates coordinates = mock(CellCoordinates.class);

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"<CELLBIND><COLUMN>a</COLUMN><CELL>A1</CELL></CELLBIND>"
                        , "A", "A1", false}
                , new Object[]{"<XXX><CELL>B7</CELL><COLUMN>A_b</COLUMN></XXX>"
                        , "A_B", "B7", false}
                , new Object[]{"<CELLBIND><COLUMN>a</COLUMN><COLUMN>a</COLUMN><CELL>A1</CELL></CELLBIND>",
                        "", "A1", true}
                , new Object[]{"<CELLBIND><COLUMN>a</COLUMN><CELL>A1</CELL><CELL>A1</CELL></CELLBIND>",
                        "", "A1", true}
                , new Object[]{"<CELLBIND><COLUMN>a</COLUMN><CELL>A1</CELL><XXX>A1</XXX></CELLBIND>",
                        "", "A1", true}
                , new Object[]{"<CELLBIND><CELL>A1</CELL></CELLBIND>",
                        "", "A1", true}
                , new Object[]{"<CELLBIND><COLUMN>a</COLUMN></CELLBIND>",
                        "", "A1", true}
                , new Object[]{"<CELLBIND><COLUMN>1</COLUMN><CELL>A1</CELL></CELLBIND>"
                        , "", "A1", true}
                , new Object[]{"<CELLBIND><COLUMN>a</COLUMN><CELL>10</CELL></CELLBIND>"
                        , "", "A1", true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String testData, String sourceColumn, String address, Boolean shouldFail) {
        when(cellCoordinatesFactory.parse(address)).thenReturn(coordinates);
        var parser = new CellBindParser(cellCoordinatesFactory);
        CellBind result;
        try (var stringReader = new StringReader(testData)) {
            final XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
            xmlReader.nextTag();
            if (shouldFail) {
                assertThatThrownBy(() -> parser.parse(xmlReader));
            } else {
                result = parser.parse(xmlReader);
                assertThat(result.getSourceColumn()).isEqualTo(sourceColumn);
                assertThat(result.getCoordinates()).isEqualTo(coordinates);
                verify(cellCoordinatesFactory).parse(address);
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("XML Stream exception", e);
        }
    }
}