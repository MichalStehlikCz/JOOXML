package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.*;
import com.provys.report.jooxml.repworkbook.RepRow;
import com.provys.report.jooxml.repworkbook.RepSheet;
import com.provys.report.jooxml.repworkbook.RepWorkbook;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellValue;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RowCellAreaTest {

    @Test
    void getNeededProcessorApplications() {
        Row row1 = mock(Row.class);
        Row row2 = mock(Row.class);
        RowCellArea testStep = new RowCellArea("TEST2", true, 5, Arrays.asList(row1, row2));
        assertThat(testStep.getNeededProcessorApplications()).isEqualTo(0);
    }

    @Test
    void getProcessorApply() {
        var row1 = mock(Row.class);
        var row2 = mock(Row.class);
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        RowCellArea testStep = new RowCellArea("TEST2", true, 5, Arrays.asList(row1, row2));
        StepProcessor processor = testStep.getProcessor(stepContext, parentRegion);
        assertThat(Stream.of(processor).flatMap(StepProcessor::apply)).
                containsExactly(processor);
    }

    @Test
    void getProcessorExecuteTopLevel() {
        var stepContext = mock(StepContext.class);
        var sheet = mock(RepSheet.class);
        var coordinates = new ContextCoordinates(sheet, 0, 0);
        var parentRegion = mock(ExecRegion.class);
        var execRegionA = mock(ExecRegion.class);
        var execRegionB = mock(ExecRegion.class);
        when(parentRegion.addArea("TEST_A", coordinates)).thenReturn(execRegionA);
        when(parentRegion.addArea("TEST_B", coordinates)).thenReturn(execRegionB);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        when(stepContext.getCoordinates()).thenReturn(coordinates);
        var workbook = mock(RepWorkbook.class);
        when(reportContext.getWorkbook()).thenReturn(workbook);
        var repRow0 = mock(RepRow.class);
        when(sheet.createRow(0, null)).thenReturn(repRow0);
        var repRow2 = mock(RepRow.class);
        when(sheet.createRow(2, null)).thenReturn(repRow2);
        var cellA0_0 = mock(AreaCell.class);
        var cellA0_3 = mock(AreaCell.class);
        var rowA0 = mock(Row.class);
        List<AreaCell> rowA0Cells = Arrays.asList(cellA0_0, cellA0_3);
        when(rowA0.getRowIndex()).thenReturn(0);
        when(rowA0.getCells()).thenReturn(rowA0Cells);
        when(cellA0_0.getColIndex()).thenReturn(0);
        var cellValueA0_0 = mock(CellValue.class);
        when(cellA0_0.getEffectiveValue(dataRecord)).thenReturn(cellValueA0_0);
        when(cellA0_0.getProperties()).thenReturn(Optional.empty());
        when(cellA0_3.getColIndex()).thenReturn(3);
        var cellValueA0_3 = mock(CellValue.class);
        when(cellA0_3.getEffectiveValue(dataRecord)).thenReturn(cellValueA0_3);
        var cellPropertiesA0_3 = mock(CellProperties.class);
        when(cellA0_3.getProperties()).thenReturn(Optional.of(cellPropertiesA0_3));
        Row rowA2 = mock(Row.class);
        AreaCell cellA2_1 = mock(AreaCell.class);
        List<AreaCell> rowA2Cells = Collections.singletonList(cellA2_1);
        when(rowA2.getRowIndex()).thenReturn(2);
        when(rowA2.getCells()).thenReturn(rowA2Cells);
        when(cellA2_1.getColIndex()).thenReturn(1);
        CellValue cellValueA2_1 = mock(CellValue.class);
        when(cellA2_1.getEffectiveValue(dataRecord)).thenReturn(cellValueA2_1);
        when(cellA2_1.getProperties()).thenReturn(Optional.empty());
        RowCellArea testStepA = new RowCellArea("TEST_A", true, 5, Arrays.asList(rowA0, rowA2));
        RepRow repRow6 = mock(RepRow.class);
        when(sheet.createRow(6, null)).thenReturn(repRow6);
        AreaCell cellB1_5 = mock(AreaCell.class);
        Row rowB1 = mock(Row.class);
        List<AreaCell> rowB1Cells = Collections.singletonList(cellB1_5);
        when(rowB1.getRowIndex()).thenReturn(1);
        when(rowB1.getCells()).thenReturn(rowB1Cells);
        when(cellB1_5.getColIndex()).thenReturn(5);
        CellValue cellValueB1_5 = mock(CellValue.class);
        when(cellB1_5.getEffectiveValue(dataRecord)).thenReturn(cellValueB1_5);
        when(cellB1_5.getProperties()).thenReturn(Optional.empty());
        RowCellArea testStepB = new RowCellArea("TEST_B", true, 2,
                Collections.singletonList(rowB1));
        Stream.of(testStepA.getProcessor(stepContext, parentRegion), testStepB.getProcessor(stepContext, parentRegion)).
                forEachOrdered(StepProcessor::execute);
        verify(repRow0).addCell(0, cellValueA0_0, null);
        verify(repRow0).addCell(3, cellValueA0_3, cellPropertiesA0_3);
        verifyNoMoreInteractions(repRow0);
        verify(repRow2).addCell(1, cellValueA2_1, null);
        verifyNoMoreInteractions(repRow2);
        verify(repRow6).addCell(5, cellValueB1_5, null);
        verifyNoMoreInteractions(repRow6);
        verify(parentRegion).addArea("TEST_A", coordinates);
        verify(parentRegion).addArea("TEST_B", coordinates);
        verifyNoMoreInteractions(parentRegion);
    }
}