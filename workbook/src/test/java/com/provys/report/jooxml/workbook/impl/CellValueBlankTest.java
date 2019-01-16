package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CellValueBlankTest {

    @Test
    void getCellTypeTest() {
        assertThat(CellValueBlank.get().getCellType()).isEqualTo(CellType.BLANK);
    }

    @Test
    public void toStringTest() {
        assertThat(CellValueBlank.get().toString()).isEqualTo("CellValueBlank{}");
    }
}