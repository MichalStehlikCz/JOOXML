package com.provys.report.jooxml.datasource;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RootDataSourceBuilderTest {

    @Test
    void getParentTest() {
        var builder = new RootDataSourceBuilder();
        assertThat(builder.getParent()).isEqualTo(Optional.empty());
    }

    @Test
    void getNameNmTest() {
        var builder = new RootDataSourceBuilder();
        assertThat(builder.getNameNm()).isEqualTo(Optional.of(RootDataSource.NAME_NM));
    }

    @Test
    void doCreateTest() {
        var builder = new RootDataSourceBuilder();
        assertThat(builder.doCreate(null)).isExactlyInstanceOf(RootDataSource.class);
    }
}