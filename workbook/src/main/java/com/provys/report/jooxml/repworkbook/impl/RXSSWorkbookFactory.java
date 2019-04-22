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

package com.provys.report.jooxml.repworkbook.impl;

import com.provys.report.jooxml.repworkbook.RepWorkbook;
import com.provys.report.jooxml.repworkbook.RepWorkbookFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Singleton
public class RXSSWorkbookFactory implements RepWorkbookFactory {
    private static Logger LOG = LogManager.getLogger(RXSSWorkbookFactory.class.getName());

    @Override
    public RepWorkbook get(File template) throws IOException {
        try (var templateStream = new FileInputStream(template)) {
            return new RXSSFWorkbook(templateStream);
        } catch (InvalidFormatException e) {
            LOG.error("Supplied template is not valid workbook {} {}", template, e);
            throw new RuntimeException("Supplied template is not valid workbook " + template, e);
        }
    }
}
