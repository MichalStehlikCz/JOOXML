package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ExecRegion;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.repexecutor.StepContext;
import com.provys.report.jooxml.repexecutor.StepProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
    void getProcessorApplyTest() {
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var execRegion1 = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 3)).thenReturn(execRegion1);
        var execRegion2 = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST2", 2)).thenReturn(execRegion2);
        var child1 = mock(ReportStep.class);
        var processor1 = mock(StepProcessor.class);
        when(child1.getProcessor(stepContext, execRegion1)).thenReturn(processor1);
        when(child1.getProcessor(stepContext, execRegion2)).thenReturn(processor1);
        var child2 = mock(ReportStep.class);
        var processor2 = mock(StepProcessor.class);
        when(child2.getProcessor(stepContext, execRegion1)).thenReturn(processor2);
        var child3 = mock(ReportStep.class);
        var processor3 = mock(StepProcessor.class);
        when(child3.getProcessor(stepContext, execRegion1)).thenReturn(processor3);
        when(child3.getProcessor(stepContext, execRegion2)).thenReturn(processor3);
        var testStep = new ParentStep("TEST", Arrays.asList(child1, child2, child3));
        assertThat(Stream.of(testStep.getProcessor(stepContext, parentRegion)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor2, processor3);
        var testStep2 = new ParentStep("TEST2", Arrays.asList(child1, child3));
        assertThat(Stream.of(testStep2.getProcessor(stepContext, parentRegion)).flatMap(StepProcessor::apply)).
                containsExactly(processor1, processor3);
    }

    @Test
    void getProcessorExecuteTest() {
        var stepContext = mock(StepContext.class);
        var parentRegion = mock(ExecRegion.class);
        var execRegion = mock(ExecRegion.class);
        when(parentRegion.addRegion("TEST", 3)).thenReturn(execRegion);
        ReportStep child1 = mock(ReportStep.class);
        StepProcessor processor1 = mock(StepProcessor.class);
        when(child1.getProcessor(stepContext, execRegion)).thenReturn(processor1);
        ReportStep child2 = mock(ReportStep.class);
        StepProcessor processor2 = mock(StepProcessor.class);
        when(child2.getProcessor(stepContext, execRegion)).thenReturn(processor2);
        ReportStep child3 = mock(ReportStep.class);
        StepProcessor processor3 = mock(StepProcessor.class);
        when(child3.getProcessor(stepContext, execRegion)).thenReturn(processor3);
        ParentStep testStep = new ParentStep("TEST", Arrays.asList(child1, child2, child3));
        assertThatThrownBy(() -> Stream.of(testStep.getProcessor(stepContext, parentRegion)).
                forEachOrdered(StepProcessor::execute));
    }
}