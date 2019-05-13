package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellReference;
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

        @Override
        public void validateCellReferences(TplWorkbook template) {}

        @Override
        public void validatePath(StepBuilder fromArea, CellReference cellReference) {
            throw new RuntimeException("Not implemented");
        }

        @Nonnull
        @Override
        public Optional<AreaCellPath> getPath(StepBuilder fromArea, CellReference cellReference) {
            throw new RuntimeException("Not implemented");
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

        /**
         * We do not want exception thrown when parent is missing, even though it is default behaviour of stepbuilder
         */
        @Override
        protected void validateParent() {}
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
        when(parent.getDataSource()).thenReturn(dataSource);
        var builder = new StepBuilderBaseImpl(parent);
        assertThat(builder.getDataSource()).isEqualTo(dataSource);
    }

    @Test
    void validateEmptyNameNmTest() {
        Map<String, ReportDataSource> dataSources = Collections.emptyMap();
        var template = mock(TplWorkbook.class);
        // default name should be used if not specified
        var builder = new StepBuilderBaseImpl(null);
        builder.validate(dataSources, template);
        assertThat(builder.getNameNm()).isEqualTo(Optional.of("DEFNAMENM"));
    }

    @Test
    void validateKeepNameNmTest() {
        Map<String, ReportDataSource> dataSources = Collections.emptyMap();
        var template = mock(TplWorkbook.class);
        // but specified name should not be touched
        var builder2 = new StepBuilderBaseImpl(null);
        builder2.setNameNm("TESTNAMENM");
        builder2.validate(dataSources, template);
        assertThat(builder2.getNameNm()).isEqualTo(Optional.of("TESTNAMENM"));
    }
}