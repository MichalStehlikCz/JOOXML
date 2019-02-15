package com.provys.report.jooxml.datasource;

import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DataSourceAncestorBuilderTest {

    private static class TestDataSourceAncestorBuilder
            extends DataSourceAncestorBuilder<TestDataSourceAncestorBuilder> {

        @Nonnull
        @Override
        TestDataSourceAncestorBuilder self() {
            return this;
        }

        @Nonnull
        @Override
        DataSourceRootAncestor doCreate(@Nullable ReportDataSource parent) {
            throw new RuntimeException("Unexpected call to doCreate");
        }
    }

    @Test
    void getParentTest() {
        var builder = new TestDataSourceAncestorBuilder();
        assertThat(builder.getParent()).isEqualTo(Optional.empty());
        var parent1 = mock(DataSourceBuilder.class);
        assertThat(builder.setParent(parent1).getParent()).isEqualTo(Optional.of(parent1));
        var parent2 = mock(DataSourceBuilder.class);
        assertThat(builder.setParent(parent2).getParent()).isEqualTo(Optional.of(parent2));
    }

    @Test
    void getNameNmTest() {
        var builder = new TestDataSourceAncestorBuilder();
        assertThat(builder.getNameNm()).isEqualTo(Optional.empty());
        assertThat(builder.setNameNm("TEST1").getNameNm()).isEqualTo(Optional.of("TEST1"));
        assertThat(builder.setNameNm("TEST2").getNameNm()).isEqualTo(Optional.of("TEST2"));
    }
}