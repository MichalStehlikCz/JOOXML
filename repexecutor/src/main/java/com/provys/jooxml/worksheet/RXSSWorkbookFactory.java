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

package com.provys.jooxml.worksheet;

import com.provys.jooxml.repexecutor.RepWorkbook;
import com.provys.jooxml.repexecutor.RepWorkbookFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class RXSSWorkbookFactory implements RepWorkbookFactory {
    @Override
    public RepWorkbook get(File template) throws IOException, InvalidFormatException {
        return new RXSSFWorkbook(template);
    }
}
