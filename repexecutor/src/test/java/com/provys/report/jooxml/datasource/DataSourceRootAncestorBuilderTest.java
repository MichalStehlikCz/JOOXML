package com.provys.report.jooxml.datasource;

import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DataSourceRootAncestorBuilderTest {

    private static class TestDataSourceRootAncestorBuilder
            extends DataSourceRootAncestorBuilder<TestDataSourceRootAncestorBuilder> {

        private final String nameNm;
        private final DataSourceRootAncestor dataSource;

        TestDataSourceRootAncestorBuilder() {
            nameNm = "TEST";
            dataSource = null;
        }

        TestDataSourceRootAncestorBuilder(@Nullable String nameNm) {
            this.nameNm = nameNm;
            dataSource = null;
        }

        TestDataSourceRootAncestorBuilder(DataSourceRootAncestor dataSource) {
            nameNm = "TEST";
            this.dataSource = dataSource;
        }

        @Nonnull
        @Override
        public Optional<DataSourceBuilder> getParent() {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<String> getNameNm() {
            return Optional.ofNullable(nameNm);
        }

        @Nonnull
        @Override
        TestDataSourceRootAncestorBuilder self() {
            return this;
        }

        @Nonnull
        @Override
        DataSourceRootAncestor doCreate(@Nullable ReportDataSource parent) {
            if (dataSource == null) {
                throw new RuntimeException("Unexpected call to doCreate");
            }
            return dataSource;
        }

    }

    @Test
    void addChildTest() {
        var builder = new TestDataSourceRootAncestorBuilder();
        assertThat(builder.getChildren()).isEmpty();
        var child1 = mock(DataSourceBuilder.class);
        assertThat(builder.addChild(child1).getChildren()).containsExactly(child1);
        var child2 = mock(DataSourceBuilder.class);
        assertThat(builder.addChild(child2).getChildren()).containsExactly(child1, child2);
    }

    @Test
    void doBuildTest() {
        var dataSource = mock(DataSourceRootAncestor.class);
        var builder = new TestDataSourceRootAncestorBuilder(dataSource);
        var child1 = mock(DataSourceBuilder.class);
        var childDataSource1 = mock(DataSourceRootAncestor.class);
        when(child1.doBuild(dataSource)).thenReturn(childDataSource1);
        builder.addChild(child1).doBuild(null);
        verify(dataSource).addChild(childDataSource1);
        verifyNoMoreInteractions(dataSource);
    }

    @Test
    void validateTest() {
        var builder = new TestDataSourceRootAncestorBuilder();
        var child1 = mock(DataSourceBuilder.class);
        when(child1.getParent()).thenReturn(Optional.of(builder));
        when(child1.getNameNm()).thenReturn(Optional.of("CHILD1"));
        var child2 = mock(DataSourceBuilder.class);
        when(child2.getParent()).thenReturn(Optional.of(builder));
        when(child2.getNameNm()).thenReturn(Optional.of("CHILD2"));
        builder.addChild(child1).addChild(child2);
        assertThatCode(builder::validate).doesNotThrowAnyException();
    }

    @Test
    void validateNoNameNmTest() {
        var builder = new TestDataSourceRootAncestorBuilder((String) null);
        assertThatThrownBy(builder::validate).hasMessageContaining("internal name is not specified");
    }

    @Test
    void validateNoParentTest() {
        var builder = new TestDataSourceRootAncestorBuilder();
        var child1 = mock(DataSourceBuilder.class);
        when(child1.getParent()).thenReturn(Optional.of(builder));
        when(child1.getNameNm()).thenReturn(Optional.of("CHILD1"));
        var child2 = mock(DataSourceBuilder.class);
        when(child2.getParent()).thenReturn(Optional.empty());
        when(child2.getNameNm()).thenReturn(Optional.of("CHILD2"));
        builder.addChild(child1).addChild(child2);
        assertThatThrownBy(builder::validate).hasMessageContaining("root data source builder as child");
    }

    @Test
    void validateWrongParentTest() {
        var builder = new TestDataSourceRootAncestorBuilder();
        var child1 = mock(DataSourceBuilder.class);
        when(child1.getParent()).thenReturn(Optional.of(builder));
        when(child1.getNameNm()).thenReturn(Optional.of("CHILD1"));
        var child2 = mock(DataSourceBuilder.class);
        when(child2.getParent()).thenReturn(Optional.of(mock(DataSourceBuilder.class)));
        when(child2.getNameNm()).thenReturn(Optional.of("CHILD2"));
        builder.addChild(child1).addChild(child2);
        assertThatThrownBy(builder::validate).hasMessageContaining("different parent");
    }

    @Test
    void validateDuplicateChildNameNmTest() {
        var builder = new TestDataSourceRootAncestorBuilder();
        var child1 = mock(DataSourceBuilder.class);
        when(child1.getParent()).thenReturn(Optional.of(builder));
        when(child1.getNameNm()).thenReturn(Optional.of("CHILD"));
        var child2 = mock(DataSourceBuilder.class);
        when(child2.getParent()).thenReturn(Optional.of(builder));
        when(child2.getNameNm()).thenReturn(Optional.of("CHILD"));
        builder.addChild(child1).addChild(child2);
        assertThatThrownBy(builder::validate).hasMessageContaining("duplicate internal name");
    }

}