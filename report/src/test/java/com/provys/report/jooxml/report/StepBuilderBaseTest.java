package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class StepBuilderBaseTest {

    private class StepBuilderBaseImpl extends StepBuilderBase<StepBuilderBaseImpl> {

        StepBuilderBaseImpl(@Nullable StepBuilder parent) {
            super(parent);
        }

        @Nonnull
        @Override
        public String getDefaultNameNmPrefix() {
            return "DEFNAMENM";
        }

        @Nonnull
        @Override
        public String proposeChildName(StepBuilder child) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public boolean isTopLevel() {
            return false;
        }

        @Nonnull
        @Override
        protected StepBuilderBaseImpl self() {
            return this;
        }

        @Nonnull
        @Override
        public ReportStep doBuild(TplWorkbook template) {
            throw new RuntimeException("Not implemented");
        }
    }

    @Test
    void getParentNoParentTest() {
        var builder = new StepBuilderBaseImpl(null);
        assertThat(builder.getParent()).isEqualTo(Optional.empty());
    }

    @Test
    void getParentParentTest() {
        var parent = mock(StepBuilder.class);
        var builder = new StepBuilderBaseImpl(parent);
        assertThat(builder.getParent()).isEqualTo(Optional.of(parent));
    }

    @Test
    void getNameNmTest() {
        var builder = new StepBuilderBaseImpl(null);
        assertThat(builder.getNameNm()).isEqualTo(Optional.empty());
        builder.setNameNm("TEST");
        assertThat(builder.getNameNm()).isEqualTo(Optional.of("TEST"));
        assertThatThrownBy(() -> builder.setNameNm("")).hasMessageContaining("empty");
    }

    @Test
    void getDataSourceTest() {
        var parent = mock(StepBuilder.class);
        var dataSource = mock(ReportDataSource.class);
        when(parent.getDataSource()).thenReturn(Optional.of(dataSource));
        var builder = new StepBuilderBaseImpl(parent);
        assertThat(builder.getDataSource()).isEqualTo(Optional.of(dataSource));
    }

    @Test
    void validateEmptyNameNmTest() {
        Map<String, ReportDataSource> dataSources = Collections.emptyMap();
        // default name should be used if not specified
        var builder = new StepBuilderBaseImpl(null);
        builder.validate(dataSources);
        assertThat(builder.getNameNm()).isEqualTo(Optional.of("DEFNAMENM"));
    }

    @Test
    void validateKeepNameNmTest() {
        Map<String, ReportDataSource> dataSources = Collections.emptyMap();
        // but specified name should not be touched
        var builder2 = new StepBuilderBaseImpl(null);
        builder2.setNameNm("TESTNAMENM");
        builder2.validate(dataSources);
        assertThat(builder2.getNameNm()).isEqualTo(Optional.of("TESTNAMENM"));
    }
}