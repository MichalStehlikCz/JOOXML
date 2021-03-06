package com.provys.report.jooxml.datasource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SelectDataSourceParserTest {

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(new Object[]{"<DATASOURCE><NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT></DATASOURCE>",
                        "TEST", "SELECT 1 FROM dual", Boolean.FALSE, Boolean.FALSE}
                , new Object[]{"<DATASOURCE><SELECT>SELECT 1 FROM dual</SELECT><NAME_NM>TEST2</NAME_NM></DATASOURCE>",
                        "TEST2", "SELECT 1 FROM dual", Boolean.FALSE, Boolean.FALSE}
                , new Object[]{"<DATASOURCE><SELECT>SELECT 1 FROM dual</SELECT><NAME_NM>TEST2</NAME_NM><TEST></TEST></DATASOURCE>",
                        "TEST2", "SELECT 1 FROM dual", Boolean.TRUE, Boolean.FALSE}
                , new Object[]{"<DATASOURCE><NAME_NM>TEST</NAME_NM></DATASOURCE>",
                        "TEST", null, Boolean.FALSE, Boolean.FALSE}
                , new Object[]{"<DATASOURCE><SELECT>SELECT 1 FROM dual</SELECT></DATASOURCE>",
                        null, "SELECT 1 FROM dual", Boolean.FALSE, Boolean.FALSE}
                , new Object[]{"<DATASOURCE><NAME_NM>TEST</NAME_NM><NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT></DATASOURCE>",
                        null, null, Boolean.FALSE, Boolean.TRUE}
                , new Object[]{"<DATASOURCE><NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT><SELECT>SELECT 1 FROM dual</SELECT></DATASOURCE>",
                        null, null, Boolean.FALSE, Boolean.TRUE}
                , new Object[]{"<DATASOURCE><NAME_NM>TEST</SECONDLEVEL></NAME_NM><SELECT>SELECT 1 FROM dual</SELECT></DATASOURCE>",
                        null, null, Boolean.FALSE, Boolean.TRUE}
                , new Object[]{"<DATASOURCE><NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 </SECONDLEVEL>FROM dual</SELECT></DATASOURCE>",
                        null, null, Boolean.FALSE, Boolean.TRUE});
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String testData, @Nullable String expNameNm, @Nullable String expSelect, Boolean hasChild,
                   Boolean shouldFail) {
        var childBuilder = mock(DataSourceBuilder.class);
        var childDataSourceParser = new ChildDataSourceParser(Map.of(TestChildDataSourceParser.TAG,
                new TestChildDataSourceParser(childBuilder)));
        var parent = mock(DataSourceBuilder.class);
        var parser = new SelectDataSourceParser(childDataSourceParser);
        try {
            var xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(testData));
            xmlReader.nextTag();
            if (shouldFail) {
                assertThatThrownBy(() -> parser.parse(parent, xmlReader));
            } else {
                var builder = parser.parse(parent, xmlReader);
                assertThat(builder.getParent().orElse(null)).isEqualTo(parent);
                assertThat(builder.getNameNm().orElse(null)).isEqualTo(expNameNm);
                assertThat(builder.getSelectStatement().orElse(null)).isEqualTo(expSelect);
                if (hasChild) {
                    assertThat(builder.getChildren()).containsExactly(childBuilder);
                } else {
                    assertThat(builder.getChildren()).isEmpty();
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("Test failed");
        }
    }
}