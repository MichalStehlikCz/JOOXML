package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DataSourceRootAncestorTest {

    private class TestDataSourceRootAncestor extends DataSourceRootAncestor {

        @Nonnull
        @Override
        public Optional<ReportDataSource> getParent() {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public String getNameNm() {
            throw new RuntimeException("Method not implemented");
        }

        @Nonnull
        @Override
        public DataContext getDataContext(ReportContext reportContext) {
            throw new RuntimeException("Method not implemented");
        }
    }

    @Test
    void getChildrenTest() {
        var test = new TestDataSourceRootAncestor();
        assertThat(test.getChildren()).isEmpty();
        var child1 = mock(ReportDataSource.class);
        when(child1.getParent()).thenReturn(Optional.of(test));
        when(child1.getNameNm()).thenReturn("CHILD1");
        test.addChild(child1);
        var child2 = mock(ReportDataSource.class);
        when(child2.getParent()).thenReturn(Optional.of(test));
        when(child2.getNameNm()).thenReturn("CHILD2");
        test.addChild(child2);
        assertThat(test.getChildren()).containsExactly(child1, child2);
    }

    @Test
    void addChildNoParentTest() {
        var test = new TestDataSourceRootAncestor();
        var child = mock(ReportDataSource.class);
        when(child.getParent()).thenReturn(Optional.empty());
        when(child.getNameNm()).thenReturn("CHILD");
        assertThatThrownBy(() -> test.addChild(child)).hasMessageContaining("root data source");
    }

    @Test
    void addChildWrongParentTest() {
        var test = new TestDataSourceRootAncestor();
        var child = mock(ReportDataSource.class);
        when(child.getParent()).thenReturn(Optional.of(mock(ReportDataSource.class)));
        when(child.getNameNm()).thenReturn("CHILD");
        assertThatThrownBy(() -> test.addChild(child)).hasMessageContaining("different parent");
    }

    @Test
    void addChildDuplicateNameNmTest() {
        var test = new TestDataSourceRootAncestor();
        var child1 = mock(ReportDataSource.class);
        when(child1.getParent()).thenReturn(Optional.of(test));
        when(child1.getNameNm()).thenReturn("CHILD");
        test.addChild(child1);
        var child2 = mock(ReportDataSource.class);
        when(child2.getParent()).thenReturn(Optional.of(test));
        when(child2.getNameNm()).thenReturn("CHILD");
        assertThatThrownBy(() -> test.addChild(child1)).hasMessageContaining("this name already exists");
    }

}