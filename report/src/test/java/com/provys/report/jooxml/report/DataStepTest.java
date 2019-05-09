package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataStepTest {

    private class DataStepImpl extends DataStep {

        DataStepImpl(String nameNm, ReportDataSource dataSource, ReportStep child) {
            super(nameNm, dataSource, child);
        }

        @Nonnull
        @Override
        public StepProcessor getProcessor(StepContext stepContext, ExecRegionContext parentRegionContext) {
            throw new RuntimeException("Method not implemented");
        }
    }

    @Test
    void getDataSource() {
        ReportDataSource dataSource = mock(ReportDataSource.class);
        ReportStep child = mock(ReportStep.class);
        DataStepImpl testStep = new DataStepImpl("TEST", dataSource, child);
        assertThat(testStep.getDataSource()).isSameAs(dataSource);
    }

    @Test
    void getChild() {
        ReportDataSource dataSource = mock(ReportDataSource.class);
        ReportStep child = mock(ReportStep.class);
        DataStepImpl testStep = new DataStepImpl("TEST", dataSource, child);
        assertThat(testStep.getChild()).isSameAs(child);
    }

    @Test
    void getNeededProcessorApplications() {
        ReportDataSource dataSource = mock(ReportDataSource.class);
        ReportStep child1 = mock(ReportStep.class);
        when(child1.getNeededProcessorApplications()).thenReturn(3);
        DataStepImpl testStep1 = new DataStepImpl("TEST", dataSource, child1);
        // one for its own processing and 3 for child
        assertThat(testStep1.getNeededProcessorApplications()).isEqualTo(4);
        ReportStep child2 = mock(ReportStep.class);
        when(child1.getNeededProcessorApplications()).thenReturn(0);
        DataStepImpl testStep2 = new DataStepImpl("TEST2", dataSource, child2);
        // one for its own processing and 0 for child
        assertThat(testStep2.getNeededProcessorApplications()).isEqualTo(1);
    }
}