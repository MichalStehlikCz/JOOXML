package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmptyCellWithBindTest {

    @Test
    void getColIndexTest() {
        assertThat(new EmptyCellWithBind(3, "TEST").getColIndex()).isEqualTo(3);
    }

    @Test
    void getCellTypeTest() {
        assertThat(new EmptyCellWithBind(0, "COLUMN").getCellType()).isEqualTo(CellType.BLANK);
    }

    @Test
    void getCellValueTest() {
        assertThat(new EmptyCellWithBind(0, "COLUMN").getCellValue()).isEmpty();
    }

    @Test
    void getEffectiveValueTest() {
        var dataRecord = mock(DataRecord.class);
        var cellValue = mock(CellValue.class);
        when(dataRecord.getCellValue("COLUMN")).thenReturn(cellValue);
        var execRegionContext = mock(ExecRegionContext.class);
        var cellPathReplacer = mock(CellPathReplacer.class);
        assertThat(new EmptyCellWithBind(0, "COLUMN").getEffectiveValue(dataRecord,
                execRegionContext, cellPathReplacer)).
                isEqualTo(cellValue);
    }

    @Test
    void getPropertiesTest() {
        assertThat(new EmptyCellWithBind(0, "COLUMN").getProperties()).
                isEmpty();
    }

    @Test
    void getBindColumnTest() {
        assertThat(new EmptyCellWithBind(0, "COLUMN").getBindColumn()).
                isEqualTo(Optional.of("COLUMN"));
    }

    @Test
    void getCellReferencesTest() {
        assertThat(new EmptyCellWithBind(0, "COLUMN").getReferenceMap()).isEmpty();
    }
}