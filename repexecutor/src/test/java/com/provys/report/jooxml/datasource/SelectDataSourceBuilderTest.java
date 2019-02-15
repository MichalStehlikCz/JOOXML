package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.Report;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SelectDataSourceBuilderTest {

    @Test
    void getSelectStatementTest() {
        var builder = new SelectDataSourceBuilder();
        assertThat(builder.getSelectStatement()).isEqualTo(Optional.empty());
        assertThat(builder.setSelectStatement("TEST1").getSelectStatement()).isEqualTo(Optional.of("TEST1"));
        assertThat(builder.setSelectStatement("TEST2").getSelectStatement()).isEqualTo(Optional.of("TEST2"));
    }

    @Test
    void validateTest() {
        var builder = new SelectDataSourceBuilder();
        builder.setNameNm("TEST_NM").setSelectStatement("TEST_SELECT");
        assertThatCode(builder::validate).doesNotThrowAnyException();
    }

    @Test
    void validateBoNameNmTest() {
        var builder = new SelectDataSourceBuilder();
        builder.setSelectStatement("TEST_SELECT");
        assertThatThrownBy(builder::validate).hasMessageContaining("internal name");
    }

    @Test
    void validateNoSelectTest() {
        var builder = new SelectDataSourceBuilder();
        builder.setNameNm("TEST_NM");
        assertThatThrownBy(builder::validate).hasMessageContaining("Select statement");
    }

    @Test
    void doCreateTest() {
        var builder = new SelectDataSourceBuilder();
        var parent = mock(ReportDataSource.class);
        var dataSource = builder.setNameNm("TEST_NM").setSelectStatement("TEST_SELECT").doCreate(parent);
        assertThat(dataSource.getParent()).isEqualTo(Optional.of(parent));
        assertThat(dataSource.getNameNm()).isEqualTo("TEST_NM");
        assertThat(dataSource.getSelectStatement()).isEqualTo("TEST_SELECT");
    }
}