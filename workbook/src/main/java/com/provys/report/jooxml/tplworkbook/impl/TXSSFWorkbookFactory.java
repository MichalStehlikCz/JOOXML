package com.provys.report.jooxml.tplworkbook.impl;

/* ====================================================================
   Copyright 2018 Michal Stehlik

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;
import com.provys.report.jooxml.workbook.CellCoordinatesFactory;
import com.provys.report.jooxml.workbook.CellReferenceFactory;
import com.provys.report.jooxml.workbook.CellValueFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@Singleton
public class TXSSFWorkbookFactory implements TplWorkbookFactory {

    @Nonnull
    private final CellCoordinatesFactory cellCoordinatesFactory;
    @Nonnull
    private final CellValueFactory cellValueFactory;
    @Nonnull
    private final CellReferenceFactory cellReferenceFactory;

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    TXSSFWorkbookFactory(CellCoordinatesFactory cellCoordinatesFactory, CellValueFactory cellValueFactory,
                         CellReferenceFactory cellReferenceFactory) {
        this.cellCoordinatesFactory = Objects.requireNonNull(cellCoordinatesFactory);
        this.cellValueFactory = Objects.requireNonNull(cellValueFactory);
        this.cellReferenceFactory = Objects.requireNonNull(cellReferenceFactory);
    }

    @Override
    @Nonnull
    public TplWorkbook get(File template) throws IOException {
        try (var templateStream = new FileInputStream(template)) {
            return new TXSSFWorkbook(templateStream, cellCoordinatesFactory, cellValueFactory, cellReferenceFactory);
        }
    }
}
