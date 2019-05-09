package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellReferenceFactory;
import com.provys.report.jooxml.workbook.CellValueFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class CellPathReplacerTest {

    private static CellReference reference1 = mock(CellReference.class);
    private static CellReference reference2 = mock(CellReference.class);

    @Nonnull
    static Stream<Object[]> pathTest() {
        return Stream.of(
                new Object[]{new CellPathCell(reference1)}
                , new Object[]{new CellPathRecord(new CellPathCell(reference1), 1)}
                , new Object[]{new CellPathRegion(new CellPathCell(reference1), "ROOT")}
                , new Object[]{new CellPathCell(reference2)}
                , new Object[]{new CellPathRecord(new CellPathCell(reference2), 5)}
                , new Object[]{new CellPathRegion(new CellPathCell(reference2), "ROOT7")}
                , new Object[]{new CellPathRegion(new CellPathRecord(new CellPathRegion(
                        new CellPathCell(reference2), "INNER"), 5), "ROOT")}
        );
    }

    /**
     * Method tests encoding / decoding of path. It just ensures that after encode / decode, result is the same but does
     * not make any assumptions regarding the intermediary format. It also verifies that encoded string matches pattern
     */
    @ParameterizedTest
    @MethodSource
    void pathTest(CellPath cellPath) {
        var address1 = "$A$1";
        when(reference1.getAddress()).thenReturn(address1);
        var address2 = "'<<a>>[[b]]/##'!AB157";
        when(reference2.getAddress()).thenReturn(address2);
        var cellReferenceFactory = mock(CellReferenceFactory.class);
        when(cellReferenceFactory.getRegex()).thenReturn("(?:(?:(?:[a-zA-Z][0-9a-zA-Z._]*)|(?:'(?:[^']|'')+'))!)?" +
                "[$]?[a-zA-Z]{1,3}[$]?[1-9][0-9]{0,5}");
        when(cellReferenceFactory.parse(address1)).thenReturn(reference1);
        when(cellReferenceFactory.parse(address2)).thenReturn(reference2);
        var cellValueFactory = mock(CellValueFactory.class);
        var cellPathReplacer = new CellPathReplacer(cellReferenceFactory, cellValueFactory);
        var encodedPath = cellPathReplacer.encodePath(cellPath);
        assertThat(cellPathReplacer.decodePath(encodedPath)).isEqualTo(cellPath);
        var matcher = CellPathReplacer.ENCODED_PATH_PATTERN.matcher(encodedPath);
        assertThat(matcher.matches()).isTrue();
    }

}