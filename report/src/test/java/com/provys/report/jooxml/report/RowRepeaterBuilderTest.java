package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.workbook.CellReference;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RowRepeaterBuilderTest {

    @Test
    void validatePathAbsoluteBeforeTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(5);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(7, 0)).thenReturn(newCellReference);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).doesNotThrowAnyException();
        verify(child).validatePath(fromRegion, newCellReference);
        verifyNoMoreInteractions(child);
    }

    @Test
    void validatePathAbsoluteAfterTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(15);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(-4, 0)).thenReturn(newCellReference);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).doesNotThrowAnyException();
        verify(child).validatePath(fromRegion, newCellReference);
        verifyNoMoreInteractions(child);
    }

    @Test
    void validatePathAbsoluteIntoTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(11);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).
                hasMessageContaining("Cannot set cell reference from outside of repeater to body");
    }

    @Test
    void validatePathRelativeBeforeTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(5);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(6, 0)).thenReturn(newCellReference);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).doesNotThrowAnyException();
        verify(child).validatePath(fromRegion, newCellReference);
        verifyNoMoreInteractions(child);
    }

    @Test
    void validatePathRelativeAfterTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(15);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(-3, 0)).thenReturn(newCellReference);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).doesNotThrowAnyException();
        verify(child).validatePath(fromRegion, newCellReference);
        verifyNoMoreInteractions(child);
    }

    @Test
    void validatePathRelativeIntoTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(12);
        when(cellReference.shiftBy(0, 0)).thenReturn(cellReference);
        assertThatCode(() -> builder.validatePath(fromRegion, cellReference)).doesNotThrowAnyException();
        verify(child).validatePath(fromRegion, cellReference);
        verifyNoMoreInteractions(child);
    }

    @Test
    void getPathAbsoluteBeforeTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(7);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(4, 0)).thenReturn(newCellReference);
        var path = mock(AreaCellPath.class);
        when(child.getPath(fromRegion, newCellReference)).thenReturn(Optional.ofNullable(path));
        assertThat(builder.getPath(fromRegion, cellReference).orElse(null)).
                isEqualTo(new AreaCellPathAbsoluteRecord(path, 1));
    }

    @Test
    void getPathAbsoluteAfterTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(18).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(14);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(-3, 0)).thenReturn(newCellReference);
        var path = mock(AreaCellPath.class);
        when(child.getPath(fromRegion, newCellReference)).thenReturn(Optional.ofNullable(path));
        assertThat(builder.getPath(fromRegion, cellReference).orElse(null)).
                isEqualTo(new AreaCellPathAbsoluteRecord(path, -2));
    }

    @Test
    void getPathAbsoluteIntoTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(false);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(11);
        assertThatCode(() -> builder.getPath(fromRegion, cellReference)).
                hasMessageContaining("Cannot set cell reference from outside of repeater to body");
    }

    @Test
    void getPathRelativeBeforeTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(16).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(7);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(3, 0)).thenReturn(newCellReference);
        var path = mock(AreaCellPath.class);
        when(child.getPath(fromRegion, newCellReference)).thenReturn(Optional.ofNullable(path));
        assertThat(builder.getPath(fromRegion, cellReference).orElse(null)).
                isEqualTo(new AreaCellPathRelativeRecord(path, -1));
    }

    @Test
    void getPathRelativeAfterTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(18).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(14);
        var newCellReference = mock(CellReference.class);
        when(cellReference.shiftBy(-3, 0)).thenReturn(newCellReference);
        var path = mock(AreaCellPath.class);
        when(child.getPath(fromRegion, newCellReference)).thenReturn(Optional.ofNullable(path));
        assertThat(builder.getPath(fromRegion, cellReference).orElse(null)).
                isEqualTo(new AreaCellPathRelativeRecord(path, 1));
    }

    @Test
    void getPathRelativeIntoTest() {
        var child = mock(RowStepBuilder.class);
        var builder = new RowRepeaterBuilder(null).
                setFirstRow(3).
                setLastRow(18).
                setFirstBodyRow(10).
                setLastBodyRow(12).
                setChild(child);
        var fromRegion = mock(StepBuilder.class);
        when(fromRegion.isAncestor(builder)).thenReturn(true);
        var cellReference = mock(CellReference.class);
        when(cellReference.getRow()).thenReturn(10);
        when(cellReference.shiftBy(0, 0)).thenReturn(cellReference);
        var path = mock(AreaCellPath.class);
        when(child.getPath(fromRegion, cellReference)).thenReturn(Optional.ofNullable(path));
        assertThat(builder.getPath(fromRegion, cellReference).orElse(null)).
                isEqualTo(new AreaCellPathRelativeRecord(path, 0));
    }
}