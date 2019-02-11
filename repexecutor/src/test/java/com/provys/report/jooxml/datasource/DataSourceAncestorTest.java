package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DataSourceAncestorTest {

    private static class TestDataSourceAncestor extends DataSourceAncestor {

        TestDataSourceAncestor(ReportDataSource parent, String nameNm) {
            super(parent, nameNm);
        }

        @Nonnull
        @Override
        public DataContext getDataContext(ReportContext reportContext) {
            throw new RuntimeException("Not implemented");
        }
    }

    @Test
    void getParent() {
        var parent = mock(ReportDataSource.class);
        var dataSource1 = new TestDataSourceAncestor(parent, "DATASOURCE");
        assertThat(dataSource1.getParent()).isEqualTo(Optional.of(parent));
    }

    @Test
    void getNameNm() {
        var parent = mock(ReportDataSource.class);
        var dataSource1 = new TestDataSourceAncestor(parent, "DATASOURCE");
        assertThat(dataSource1.getNameNm()).isEqualTo("DATASOURCE");
    }
}