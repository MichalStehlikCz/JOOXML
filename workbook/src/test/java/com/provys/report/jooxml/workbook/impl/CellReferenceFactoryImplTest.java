package com.provys.report.jooxml.workbook.impl;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CellReferenceFactoryImplTest {

    @Test
    void of() {
        var factory = new CellReferenceFactoryImpl();
        var coordinates = CellCoordinatesInt.of(1, 2);
        var cellReference = factory.of("Sheet", coordinates);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.of("Sheet"));
        assertThat(cellReference.getCoordinates()).isEqualTo(coordinates);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(false);
    }

    @Test
    void of1() {
        var factory = new CellReferenceFactoryImpl();
        var coordinates = CellCoordinatesInt.of(1, 2);
        var cellReference = factory.of(coordinates);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.empty());
        assertThat(cellReference.getCoordinates()).isEqualTo(coordinates);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(false);
    }

    @Test
    void of2() {
        var factory = new CellReferenceFactoryImpl();
        var cellReference = factory.of("Sheet", 5, 4);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.of("Sheet"));
        assertThat(cellReference.getRow()).isEqualTo(5);
        assertThat(cellReference.getCol()).isEqualTo(4);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(false);
    }

    @Test
    void of3() {
        var factory = new CellReferenceFactoryImpl();
        var cellReference = factory.of(5, 4);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.empty());
        assertThat(cellReference.getRow()).isEqualTo(5);
        assertThat(cellReference.getCol()).isEqualTo(4);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(false);
    }

    @Test
    void of4() {
        var factory = new CellReferenceFactoryImpl();
        var coordinates = CellCoordinatesInt.of(1, 2);
        var cellReference = factory.of("Sheet", coordinates, false, true);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.of("Sheet"));
        assertThat(cellReference.getCoordinates()).isEqualTo(coordinates);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(true);
    }

    @Test
    void of5() {
        var factory = new CellReferenceFactoryImpl();
        var coordinates = CellCoordinatesInt.of(1, 2);
        var cellReference = factory.of(coordinates, true, true);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.empty());
        assertThat(cellReference.getCoordinates()).isEqualTo(coordinates);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(true);
        assertThat(cellReference.isColAbsolute()).isEqualTo(true);
    }

    @Test
    void of6() {
        var factory = new CellReferenceFactoryImpl();
        var cellReference = factory.of("Sheet", 5, 4, true, false);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.of("Sheet"));
        assertThat(cellReference.getRow()).isEqualTo(5);
        assertThat(cellReference.getCol()).isEqualTo(4);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(true);
        assertThat(cellReference.isColAbsolute()).isEqualTo(false);
    }

    @Test
    void of7() {
        var factory = new CellReferenceFactoryImpl();
        var cellReference = factory.of(5, 4, false, true);
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.empty());
        assertThat(cellReference.getRow()).isEqualTo(5);
        assertThat(cellReference.getCol()).isEqualTo(4);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(false);
        assertThat(cellReference.isColAbsolute()).isEqualTo(true);
    }

    @Test
    void getRegexTest() {
        var factory = new CellReferenceFactoryImpl();
        assertThat(factory.getRegex()).isEqualTo(CellReferenceInt.REGEX);
    }

    @Test
    void parse() {
        var factory = new CellReferenceFactoryImpl();
        var cellReference = factory.parse("Sheet!$B$7");
        assertThat(cellReference.getSheetName()).isEqualTo(Optional.of("Sheet"));
        assertThat(cellReference.getRow()).isEqualTo(6);
        assertThat(cellReference.getCol()).isEqualTo(1);
        assertThat(cellReference.isRowAbsolute()).isEqualTo(true);
        assertThat(cellReference.isColAbsolute()).isEqualTo(true);
    }
}