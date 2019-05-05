package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class DataReaderTest {

    @Test
    void getProcessorApplyCorrectTest() {
        // correct (single row data-set)
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var coordinates = mock(ContextCoordinates.class);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        var dataRecord1 = mock(DataRecord.class);
        when(dataCursor.getData()).thenReturn(Stream.of(dataRecord1));
        var stepContext1 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord1)).thenReturn(stepContext1);
        var execRegion = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 1)).thenReturn(execRegion);
        var child = mock(ReportStep.class);
        var processor = mock(StepProcessor.class);
        when(child.getProcessor(stepContext1, execRegion)).thenReturn(processor);
        var testStep = new DataReader("TEST", dataSource, child);
        assertThat(Stream.of(testStep.getProcessor(stepContext, parentRegion)).flatMap(StepProcessor::apply)).
                containsExactly(processor);
    }

    @Test
    void getProcessorApplyNoDataTest() {
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var execRegion = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 1)).thenReturn(execRegion);
        var child = mock(ReportStep.class);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        when(dataCursor.getData()).thenReturn(Stream.of());
        var testStep = new DataReader("TEST", dataSource, child);
        //noinspection ResultOfMethodCallIgnored - we need aggregator to execute stream, not to get result
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext, parentRegion)).flatMap(StepProcessor::apply).
                count()).
                isInstanceOf(RuntimeException.class).hasMessageContaining("any data");
    }

    @Test
    void getProcessorApplyTooManyRowsTest() {
        // incorrect (multi-row data-set)
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var coordinates = mock(ContextCoordinates.class);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        var dataRecord1 = mock(DataRecord.class);
        var dataRecord2 = mock(DataRecord.class);
        when(dataCursor.getData()).thenReturn(Stream.of(dataRecord1, dataRecord2));
        var execRegion = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 1)).thenReturn(execRegion);
        var child = mock(ReportStep.class);
        var processor = mock(StepProcessor.class);
        var testStep = new DataReader("TEST", dataSource, child);
        //noinspection ResultOfMethodCallIgnored - count is used to trigger processing of stream, result doesn't matter
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext, parentRegion)).
                flatMap(StepProcessor::apply).count()).
                isInstanceOf(RuntimeException.class).hasMessageContaining("returned more");
    }

    @Test
    void getProcessorExecuteTest() {
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        var coordinates = mock(ContextCoordinates.class);
        var dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        var dataSource = mock(ReportDataSource.class);
        var dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        var dataCursor = mock(DataCursor.class);
        when(dataContext.execute(dataRecord)).thenReturn(dataCursor);
        var dataRecord1 = mock(DataRecord.class);
        when(dataCursor.getData()).thenReturn(Stream.of(dataRecord1));
        var stepContext1 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord1)).thenReturn(stepContext1);
        var execRegion = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 1)).thenReturn(execRegion);
        var child = mock(ReportStep.class);
        var processor = mock(StepProcessor.class);
        when(child.getProcessor(stepContext1, execRegion)).thenReturn(processor);
        var testStep = new DataReader("TEST", dataSource, child);
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext, parentRegion)).
                forEachOrdered(StepProcessor::execute));
    }
}