package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ParameterReader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SelectDataSourceParserTest {

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(new Object[]{"<NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT>",
                        new SelectDataSourceBuilder().setNameNm("TEST").setSelectStatement("SELECT 1 FROM dual"),
                        Boolean.FALSE}
                , new Object[]{"<SELECT>SELECT 1 FROM dual</SELECT><NAME_NM>TEST2</NAME_NM>",
                        new SelectDataSourceBuilder().setNameNm("TEST2").setSelectStatement("SELECT 1 FROM dual"),
                        Boolean.FALSE}
                , new Object[]{"<NAME_NM>TEST</NAME_NM>",
                        new SelectDataSourceBuilder().setNameNm("TEST"), Boolean.FALSE}
                , new Object[]{"<SELECT>SELECT 1 FROM dual</SELECT>",
                        new SelectDataSourceBuilder().setSelectStatement("SELECT 1 FROM dual"), Boolean.TRUE}
                , new Object[]{"<NAME_NM>TEST</NAME_NM><NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT>",
                        null, Boolean.TRUE}
                , new Object[]{"<NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 FROM dual</SELECT><SELECT>SELECT 1 FROM dual</SELECT>",
                        null, Boolean.TRUE}
                , new Object[]{"<NAME_NM>TEST</SECONDLEVEL></NAME_NM><SELECT>SELECT 1 FROM dual</SELECT>",
                        null, Boolean.TRUE}
                , new Object[]{"<NAME_NM>TEST</NAME_NM><SELECT>SELECT 1 </SECONDLEVEL>FROM dual</SELECT>",
                        null, Boolean.TRUE});
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String testData, @Nullable SelectDataSourceBuilder expResult, Boolean shouldFail) {
        var dataSourceParser = mock(DataSourceParser.class);
        var parser = new SelectDataSourceParser(dataSourceParser);
        try {
            var xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(testData));
            if (shouldFail) {
                assertThatThrownBy(() -> parser.parse(null, xmlReader));
            } else {
                assertThat(parser.parse(null, xmlReader)).isEqualTo(expResult);
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}