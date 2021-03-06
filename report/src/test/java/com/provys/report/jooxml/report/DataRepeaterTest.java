package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataRepeaterTest {

    @Test
    void getProcessorApplyTest() {
        var stepContext = mock(StepContext.class);
        var parentRegionContext = mock(ExecRegionContext.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        var dataRecord1 = mock(DataRecord.class);
        var dataRecord2 = mock(DataRecord.class);
        var dataRecord3 = mock(DataRecord.class);
        when(dataCursor.getData()).thenReturn(Stream.of(dataRecord1, dataRecord2, dataRecord3));
        var stepContext1 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord1)).thenReturn(stepContext1);
        var stepContext2 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord2)).thenReturn(stepContext2);
        var stepContext3 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord3)).thenReturn(stepContext3);
        var execRegionContext = mock(ExecRegionContext.class);
        when(parentRegionContext.addTable("TEST")).thenReturn(execRegionContext);
        var child = mock(ReportStep.class);
        var processor1 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext1, execRegionContext)).thenReturn(processor1);
        var processor2 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext2, execRegionContext)).thenReturn(processor2);
        var processor3 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext3, execRegionContext)).thenReturn(processor3);
        var testStep = new DataRepeater("TEST", dataSource, child);
        var closeProcessor = new CloseDataCursorProcessor(testStep, dataCursor);
        assertThat(Stream.of(testStep.getProcessor(stepContext, parentRegionContext)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor2, processor3, closeProcessor);
    }

    @Test
    void getProcessorApplyNoDataTest() {
        var stepContext = mock(StepContext.class);
        var parentRegionContext = mock(ExecRegionContext.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var execRegionContext = mock(ExecRegionContext.class);
        when(parentRegionContext.addTable("TEST")).thenReturn(execRegionContext);
        var child = mock(ReportStep.class);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        when(dataCursor.getData()).thenReturn(Stream.of());
        var testStep = new DataRepeater("TEST", dataSource, child);
        var closeProcessor = new CloseDataCursorProcessor(testStep, dataCursor);
        assertThat(Stream.of(testStep.getProcessor(stepContext, parentRegionContext)).flatMap(StepProcessor::apply)).
                containsExactly(closeProcessor);
    }

    @Test
    void getProcessorExecuteTest() {
        var stepContext = mock(StepContext.class);
        var parentRegionContext = mock(ExecRegionContext.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var execRegionContext = mock(ExecRegionContext.class);
        when(parentRegionContext.addTable("TEST")).thenReturn(execRegionContext);
        var child = mock(ReportStep.class);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        when(dataCursor.getData()).thenReturn(Stream.of());
        var testStep = new DataRepeater("TEST", dataSource, child);
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext, parentRegionContext)).
                forEachOrdered(StepProcessor::execute));
    }
}