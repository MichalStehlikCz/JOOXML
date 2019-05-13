package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateCellWithBindTest {

    @Test
    void getColIndexTest() {
        var tplCell = mock(TplCell.class);
        var stepBuilder = mock(StepBuilder.class);
        assertThat(new TemplateCellWithBind(3, tplCell, stepBuilder, null).getColIndex()).
                isEqualTo(3);
    }

    @Test
    void getCellTypeTest() {
        var tplCell = mock(TplCell.class);
        when(tplCell.getCellType()).thenReturn(CellType.STRING);
        var stepBuilder = mock(StepBuilder.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, null).getCellType()).
                isEqualTo(CellType.STRING);
    }

    @Test
    void getCellValueTest() {
        var tplCell = mock(TplCell.class);
        var cellValue = mock(CellValue.class);
        when(tplCell.getCellValue()).thenReturn(cellValue);
        var stepBuilder = mock(StepBuilder.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, null).getCellValue()).
                contains(cellValue);
    }

    @Test
    void getEffectiveValueBindTest() {
        var tplCell = mock(TplCell.class);
        when(tplCell.getCellType()).thenReturn(CellType.STRING);
        var dataRecord = mock(DataRecord.class);
        var cellValue = mock(CellValue.class);
        when(dataRecord.getCellValue("COLUMN", CellType.STRING)).thenReturn(cellValue);
        when(tplCell.getCellValue()).thenReturn(cellValue);
        var stepBuilder = mock(StepBuilder.class);
        var execRegionContext = mock(ExecRegionContext.class);
        var cellPathReplacer = mock(CellPathReplacer.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, "COLUMN").
                getEffectiveValue(dataRecord, execRegionContext, cellPathReplacer)).isEqualTo(cellValue);
    }

    @Test
    void getEffectiveValueNoBindTest() {
        var tplCell = mock(TplCell.class);
        var cellValue = mock(CellValue.class);
        var dataRecord = mock(DataRecord.class);
        when(tplCell.getCellValue()).thenReturn(cellValue);
        var stepBuilder = mock(StepBuilder.class);
        var execRegionContext = mock(ExecRegionContext.class);
        var cellPathReplacer = mock(CellPathReplacer.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, null).
                getEffectiveValue(dataRecord, execRegionContext, cellPathReplacer)).isEqualTo(cellValue);
    }

    @Test
    void getPropertiesTest() {
        var tplCell = mock(TplCell.class);
        when(tplCell.getCellProperties()).thenReturn(Optional.empty());
        var stepBuilder = mock(StepBuilder.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, null).getProperties()).isEmpty();
        var tplCell2 = mock(TplCell.class);
        var cellProperties = mock(CellProperties.class);
        when(tplCell2.getCellProperties()).thenReturn(Optional.of(cellProperties));
        assertThat(new TemplateCellWithBind(2, tplCell2, stepBuilder, "TEST").getProperties()).
                contains(cellProperties);
    }

    @Test
    void getBindColumnTest() {
        var tplCell = mock(TplCell.class);
        var stepBuilder = mock(StepBuilder.class);
        assertThat(new TemplateCellWithBind(0, tplCell, stepBuilder, null).getBindColumn()).isEmpty();
        assertThat(new TemplateCellWithBind(2, tplCell, stepBuilder, "TEST").getBindColumn()).contains("TEST");
    }
}