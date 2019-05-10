package com.provys.report.jooxml.tplworkbook.impl;

import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellReferenceFactory;
import com.provys.report.jooxml.workbook.CellValueFactory;
import com.provys.report.jooxml.workbook.impl.CellReferenceFactoryImpl;
import com.provys.report.jooxml.workbook.impl.CellValueFactoryImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TXSSFCellTest {

    private static final CellValueFactory cellValueFactory = new CellValueFactoryImpl();
    private static final CellReferenceFactory cellReferenceFactory = new CellReferenceFactoryImpl();
    private static final TXSSFWorkbook workbook = mock(TXSSFWorkbook.class);
    private static final TXSSFSheet sheet = mock(TXSSFSheet.class);
    private static final TXSSFRow row = mock(TXSSFRow.class);

    static {
        when(workbook.getCellValueFactory()).thenReturn(cellValueFactory);
        when(workbook.getCellReferenceFactory()).thenReturn(cellReferenceFactory);
        when(sheet.getWorkbook()).thenReturn(workbook);
        when(row.getSheet()).thenReturn(sheet);
    }

    @Nonnull
    static Stream<Object[]> getReferenceMapFormulaTest() {
        return Stream.of(
                new Object[]{"$A$11", new String[]{"$A$11"}}
                , new Object[]{"1", new String[]{}}
                , new Object[]{"1+A2", new String[]{"A2"}}
                , new Object[]{"1+AB22", new String[]{"AB22"}}
                , new Object[]{"1+$B$2", new String[]{"$B$2"}}
                , new Object[]{"A1+$B$2", new String[]{"A1", "$B$2"}}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getReferenceMapFormulaTest(String formula, String[] references) {
        var cell = mock(Cell.class);
        when(cell.getColumnIndex()).thenReturn(0);
        when(cell.getCellType()).thenReturn(CellType.FORMULA);
        when(cell.getCellFormula()).thenReturn(formula);
        var referenceMap = new HashMap<String, CellReference>();
        for (var reference : references) {
            referenceMap.put(reference, cellReferenceFactory.parse(reference));
        }
        var tplCell = new TXSSFCell(row, cell);
        assertThat(tplCell.getReferenceMap()).containsExactlyEntriesOf(referenceMap);
    }
}