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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RowRegionBuilderTest {

    private class RowRegionBuilderImpl extends RowRegionBuilder<RowRegionBuilderImpl> {

        RowRegionBuilderImpl(@Nullable StepBuilder parent) {
            super(parent);
        }

        @Nonnull
        @Override
        public String getDefaultNameNmPrefix() {
            return "ROWREGIONBUILDERIMPL";
        }

        @Nonnull
        @Override
        public String proposeChildName(StepBuilder child) {
            throw new RuntimeException("Not implemented");
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
        protected RowRegionBuilderImpl self() {
            return this;
        }

        @Nonnull
        @Override
        public ReportStep doBuild(TplWorkbook template) {
            throw new RuntimeException("Not implemented");
        }
    }

    @Test
    void getFirstRowTest() {
        var builder = new RowRegionBuilderImpl(null);
        assertThat(builder.getFirstRow()).isEqualTo(Optional.empty());
        builder.setFirstRow(1);
        assertThat(builder.getFirstRow()).isEqualTo(Optional.of(1));
        builder.setEffFirstRow(2);
        assertThat(builder.getFirstRow()).isEqualTo(Optional.of(2));
    }

    @Test
    void getEffFirstRowTest() {
        var builder = new RowRegionBuilderImpl(null);
        assertThat(builder.getEffFirstRow()).isEqualTo(Optional.empty());
        builder.setFirstRow(1);
        assertThat(builder.getEffFirstRow()).isEqualTo(Optional.of(1));
        builder.setEffFirstRow(2);
        assertThat(builder.getEffFirstRow()).isEqualTo(Optional.of(2));
    }

    @Test
    void getLastRowTest() {
        var builder = new RowRegionBuilderImpl(null);
        assertThat(builder.getLastRow()).isEqualTo(Optional.empty());
        builder.setLastRow(1);
        assertThat(builder.getLastRow()).isEqualTo(Optional.of(1));
        builder.setEffLastRow(2);
        assertThat(builder.getLastRow()).isEqualTo(Optional.of(2));
    }

    @Test
    void getEffLastRowTest() {
        var builder = new RowRegionBuilderImpl(null);
        assertThat(builder.getEffLastRow()).isEqualTo(Optional.empty());
        builder.setLastRow(1);
        assertThat(builder.getEffLastRow()).isEqualTo(Optional.of(1));
        builder.setEffLastRow(2);
        assertThat(builder.getEffLastRow()).isEqualTo(Optional.of(2));
    }

    @Test
    void isTopLevelTest() {
        var parent = mock(StepBuilder.class);
        when(parent.isTopLevel()).thenReturn(true);
        var builder = new RowRegionBuilderImpl(parent);
        assertThat(builder.isTopLevel()).isTrue();
        when(parent.isTopLevel()).thenReturn(false);
        assertThat(builder.isTopLevel()).isFalse();
    }

    @Test
    void validateTest() {
        Map<String, ReportDataSource> dataSources = Collections.emptyMap();
        var template = mock(TplWorkbook.class);
        var parent = mock(StepBuilder.class);
        var builder = new RowRegionBuilderImpl(parent);
        // child name is evaluated during validation
        when(parent.proposeChildName(builder)).thenReturn("CHILD");
        assertThatThrownBy(() -> builder.validate(dataSources, template)).hasMessageContaining("First row");
        builder.setFirstRow(5);
        assertThatThrownBy(() -> builder.validate(dataSources, template)).hasMessageContaining("Last row");
        builder.setLastRow(4);
        assertThatThrownBy(() -> builder.validate(dataSources, template)).hasMessageContaining("above last row");
        builder.setFirstRow(3);
        assertThatCode(() -> builder.validate(dataSources, template)).doesNotThrowAnyException();
    }
}