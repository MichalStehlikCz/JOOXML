package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellReference;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateCellWithBindTest {

    @Test
    void getColIndexTest() {
        var tplCell = mock(TplCell.class);
        assertThat(new TemplateCellWithBind(3, tplCell, null).getColIndex()).
                isEqualTo(3);
    }

    @Test
    void getCellTypeTest() {
        var tplCell = mock(TplCell.class);
        when(tplCell.getCellType()).thenReturn(CellType.STRING);
        assertThat(new TemplateCellWithBind(0, tplCell, null).getCellType()).
                isEqualTo(CellType.STRING);
    }

    @Test
    void getCellValueTest() {
        var tplCell = mock(TplCell.class);
        var cellValue = mock(CellValue.class);
        when(tplCell.getCellValue()).thenReturn(cellValue);
        assertThat(new TemplateCellWithBind(0, tplCell, null).getCellValue()).
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
        assertThat(new TemplateCellWithBind(0, tplCell, "COLUMN").getEffectiveValue(dataRecord)).
                isEqualTo(cellValue);
    }

    @Test
    void getEffectiveValueNoBindTest() {
        var tplCell = mock(TplCell.class);
        var cellValue = mock(CellValue.class);
        var dataRecord = mock(DataRecord.class);
        when(tplCell.getCellValue()).thenReturn(cellValue);
        assertThat(new TemplateCellWithBind(0, tplCell, null).getEffectiveValue(dataRecord)).
                isEqualTo(cellValue);
    }

    @Test
    void getPropertiesTest() {
        var tplCell = mock(TplCell.class);
        when(tplCell.getCellProperties()).thenReturn(Optional.empty());
        assertThat(new TemplateCellWithBind(0, tplCell, null).getProperties()).isEmpty();
        var tplCell2 = mock(TplCell.class);
        var cellProperties = mock(CellProperties.class);
        when(tplCell2.getCellProperties()).thenReturn(Optional.of(cellProperties));
        assertThat(new TemplateCellWithBind(2, tplCell2, "TEST").getProperties()).
                contains(cellProperties);
    }

    @Test
    void getBindColumnTest() {
        var tplCell = mock(TplCell.class);
        assertThat(new TemplateCellWithBind(0, tplCell, null).getBindColumn()).isEmpty();
        assertThat(new TemplateCellWithBind(2, tplCell, "TEST").getBindColumn()).contains("TEST");
    }

    @Test
    void getCellReferencesTest() {
        var tplCell = mock(TplCell.class);
        var cellReference = mock(CellReference.class);
        when(tplCell.getCellReferences()).thenReturn(Collections.singletonList(cellReference));
        assertThat(new TemplateCellWithBind(0, tplCell, null).getCellReferences()).
                containsExactly(cellReference);
    }
}