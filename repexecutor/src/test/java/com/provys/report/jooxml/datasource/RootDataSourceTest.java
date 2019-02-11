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
        assertThat(RootDataSource.getInstance().getParent()).isEqualTo(Optional.empty());
    }

    @Test
    void getNameNmTest() {
        assertThat(RootDataSource.getInstance().getNameNm()).isEqualTo("ROOT");
    }

    @Test
    void getDataContextTest() {
        var reportContext = mock(ReportContext.class);
        DataContext dataContext = RootDataSource.getInstance().getDataContext(reportContext);
        dataContext.prepare();
        assertThat(dataContext.getClass()).isEqualTo(RootDataContext.class);
        dataContext.close();
    }
}