package com.provys.report.jooxml.template;

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

import com.provys.report.jooxml.report.TemplateWorkbook;
import com.provys.report.jooxml.report.TemplateWorkbookFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class TXSSFWorkbookFactory implements TemplateWorkbookFactory {
    @Override
    public TemplateWorkbook get(File template) throws IOException, InvalidFormatException {
        return new TXSSFWorkbook(template);
    }
}
