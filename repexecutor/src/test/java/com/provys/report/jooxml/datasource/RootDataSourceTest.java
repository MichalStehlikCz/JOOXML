package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataContext;
import com.provys.report.jooxml.repexecutor.ReportContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RootDataSourceTest {

    @Test
    void getParentTest() {
        assertThat(new RootDataSource().getParent()).isEqualTo(Optional.empty());
    }

    @Test
    void getNameNmTest() {
        assertThat(new RootDataSource().getNameNm()).isEqualTo("ROOT");
    }

    @Test
    void getDataContextTest() {
        var reportContext = mock(ReportContext.class);
        DataContext dataContext = new RootDataSource().getDataContext(reportContext);
        dataContext.prepare();
        assertThat(dataContext.getClass()).isEqualTo(RootDataContext.class);
        dataContext.close();
    }
}