package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataRepeaterTest {

    @Test
    void getProcessorTest() {
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
        DataRecord dataRecord3 = mock(DataRecord.class);
        when(dataContext.execute(dataRecord)).thenReturn(Stream.of(dataRecord1, dataRecord2, dataRecord3));
        StepContext stepContext1 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord1)).thenReturn(stepContext1);
        StepContext stepContext2 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord2)).thenReturn(stepContext2);
        StepContext stepContext3 = mock(StepContext.class);
        when(stepContext.cloneWithReplaceData(dataRecord3)).thenReturn(stepContext3);
        ReportStep child = mock(ReportStep.class);
        StepProcessor processor1 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext1)).thenReturn(processor1);
        StepProcessor processor2 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext2)).thenReturn(processor2);
        StepProcessor processor3 = mock(StepProcessor.class);
        when(child.getProcessor(stepContext3)).thenReturn(processor3);
        DataRepeater testStep = new DataRepeater("TEST", dataSource, child);
        assertThat(Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor2, processor3);
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
        DataRepeater testStep = new DataRepeater("TEST", dataSource, child);
        assertThat(Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply)).
                isEmpty();
    }
}