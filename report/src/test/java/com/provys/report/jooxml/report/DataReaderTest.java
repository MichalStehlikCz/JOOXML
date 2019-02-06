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
    void getProcessorCorrectTest() {
        // correct (single row dataset)
        StepContext stepContext = mock(StepContext.class);
        ReportContext reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        ContextCoordinates coordinates = mock(ContextCoordinates.class);
        DataRecord dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        ReportDataSource dataSource = mock(ReportDataSource.class);
        DataContext dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        DataRecord dataRecord1 = mock(DataRecord.class);
        when(dataContext.execute(dataRecord)).thenReturn(Stream.of(dataRecord1));
        StepContext stepContext1 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord1)).thenReturn(stepContext1);
        ReportStep child = mock(ReportStep.class);
        StepProcessor processor = mock(StepProcessor.class);
        when(child.getProcessor(stepContext1)).thenReturn(processor);
        DataReader testStep = new DataReader("TEST", dataSource, child);
        assertThat(Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply)).
                containsExactly(processor);
    }

    @Test
    void getProcessorNoDataTest() {
        StepContext stepContext = mock(StepContext.class);
        ReportContext reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        ContextCoordinates coordinates = mock(ContextCoordinates.class);
        DataRecord dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        ReportDataSource dataSource = mock(ReportDataSource.class);
        DataContext dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        ReportStep child = mock(ReportStep.class);
        when(dataContext.execute(dataRecord)).thenReturn(Stream.of());
        DataReader testStep = new DataReader("TEST", dataSource, child);
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply).
                count()).
                isInstanceOf(RuntimeException.class).hasMessageContaining("any data");
    }

    @Test
    void getProcessorTooManyRowsTest() {
        // correct (single row dataset)
        StepContext stepContext = mock(StepContext.class);
        ReportContext reportContext = mock(ReportContext.class);
        when(stepContext.getReportContext()).thenReturn(reportContext);
        ContextCoordinates coordinates = mock(ContextCoordinates.class);
        DataRecord dataRecord = mock(DataRecord.class);
        when(stepContext.getData()).thenReturn(dataRecord);
        ReportDataSource dataSource = mock(ReportDataSource.class);
        DataContext dataContext = mock(DataContext.class);
        when(reportContext.getDataContext(dataSource)).thenReturn(dataContext);
        DataRecord dataRecord1 = mock(DataRecord.class);
        DataRecord dataRecord2 = mock(DataRecord.class);
        when(dataContext.execute(dataRecord)).thenReturn(Stream.of(dataRecord1, dataRecord2));
        ReportStep child = mock(ReportStep.class);
        StepProcessor processor = mock(StepProcessor.class);
        DataReader testStep = new DataReader("TEST", dataSource, child);
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply).count()).
                isInstanceOf(RuntimeException.class).hasMessageContaining("returned more");
    }
}