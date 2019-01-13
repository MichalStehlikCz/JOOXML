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

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
@Singleton
public class TXSSFWorkbookFactory implements TplWorkbookFactory {
    @Override
    public TplWorkbook get(File template) throws IOException {
        return new TXSSFWorkbook(template);
    }
}
