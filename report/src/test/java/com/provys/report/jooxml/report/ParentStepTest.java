package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentStepTest {

    @Test
    void getNeededProcessorApplicationsTest() {
        ReportStep child1 = mock(ReportStep.class);
        when(child1.getNeededProcessorApplications()).thenReturn(3);
        ReportStep child2 = mock(ReportStep.class);
        when(child2.getNeededProcessorApplications()).thenReturn(5);
        ReportStep child3 = mock(ReportStep.class);
        when(child3.getNeededProcessorApplications()).thenReturn(0);
        ParentStep testStep = new ParentStep("TEST", Arrays.asList(child1, child2, child3));
        // one for its own processing and 5 for biggest child
        assertThat(testStep.getNeededProcessorApplications()).isEqualTo(6);
        ParentStep testStep2 = new ParentStep("TEST2", Arrays.asList(child1, child3));
        // one for its own processing and 3 for biggest child
        assertThat(testStep2.getNeededProcessorApplications()).isEqualTo(4);
    }

    @Test
    void getProcessor() {
        StepContext stepContext = mock(StepContext.class);
        ReportStep child1 = mock(ReportStep.class);
        StepProcessor processor1 = mock(StepProcessor.class);
        when(child1.getProcessor(stepContext)).thenReturn(processor1);
        ReportStep child2 = mock(ReportStep.class);
        StepProcessor processor2 = mock(StepProcessor.class);
        when(child2.getProcessor(stepContext)).thenReturn(processor2);
        ReportStep child3 = mock(ReportStep.class);
        StepProcessor processor3 = mock(StepProcessor.class);
        when(child3.getProcessor(stepContext)).thenReturn(processor3);
        ParentStep testStep = new ParentStep("TEST", Arrays.asList(child1, child2, child3));
        assertThat(Stream.of(testStep.getProcessor(stepContext)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor2, processor3);
        ParentStep testStep2 = new ParentStep("TEST2", Arrays.asList(child1, child3));
        assertThat(Stream.of(testStep2.getProcessor(stepContext)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor3);
    }

    @Test
    void getChildren() {
        ReportStep child1 = mock(ReportStep.class);
        ReportStep child2 = mock(ReportStep.class);
        ReportStep child3 = mock(ReportStep.class);
        ParentStep testStep = new ParentStep("TEST", Arrays.asList(child1, child2, child3));
        assertThat(testStep.getChildren()).containsExactly(child1, child2, child3);
    }
}