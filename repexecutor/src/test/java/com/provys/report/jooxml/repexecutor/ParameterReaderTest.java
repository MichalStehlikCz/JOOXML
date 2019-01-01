package com.provys.report.jooxml.repexecutor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.*;

class ParameterReaderTest {

    static Stream<Object[]> getParametersFromString() {
        return Stream.of(new Object[]{"<PARAMETERS><TESTNAME>TESTVALUE</TESTNAME></PARAMETERS>"
                        , new Parameter[]{new Parameter("TESTNAME", "TESTVALUE")}, Boolean.FALSE}
                , new Object[]{"<PARAMETERS><TESTNAME>TESTVALUE</TESTNAME><EMPTYPARAM/></PARAMETERS>"
                        , new Parameter[]{new Parameter("TESTNAME", "TESTVALUE")
                        , new Parameter("EMPTYPARAM", (String) null)}, Boolean.FALSE}
                , new Object[]{"<PARAMETERS><TESTNAME><SECONDLEVEL/></TESTNAME></PARAMETERS>", null, Boolean.TRUE}
                , new Object[]{"<SOMETHINGELSE><TESTNAME>TESTVALUE</TESTNAME></SOMETHINGELSE>", null, Boolean.TRUE});
    }

    @ParameterizedTest
    @MethodSource
    void getParametersFromString(String testData, Parameter[] expResult, Boolean shouldFail) {
        try (ParameterReader paramReader = new ParameterReader(new StringReader(testData))) {
            if (shouldFail) {
                assertThatThrownBy(paramReader::getParameters);
            } else {
                assertThat(paramReader.getParameters()).containsExactlyInAnyOrder(expResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}