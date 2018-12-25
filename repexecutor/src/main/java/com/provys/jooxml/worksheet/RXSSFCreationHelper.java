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

   Derived from work that is part of Apache POI project, licenced by
   the Apache Software Foundation (ASF) under Apache Licnece, Version 2.0
==================================================================== */

package com.provys.jooxml.worksheet;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * Streaming Creation Helper, which performs some actions
 *  based on the Streaming Workbook, and some on the related
 *  regular XSSF Workbook
 */
public class RXSSFCreationHelper implements CreationHelper {
    private static final POILogger logger = POILogFactory.getLogger(RXSSFCreationHelper.class);
    
    private final RXSSFWorkbook wb;
    private final XSSFCreationHelper helper;
    
    /**
     * Should only be called by {@link RXSSFWorkbook#getCreationHelper()}
     *
     * @param workbook the workbook to create objects for
     */
    @Internal
    public RXSSFCreationHelper(RXSSFWorkbook workbook) {
        this.helper = new XSSFCreationHelper(workbook.getXSSFWorkbook());
        this.wb = workbook;
    }

    @Override
    public XSSFRichTextString createRichTextString(String text) {
        logger.log(POILogger.INFO, "SXSSF doesn't support Rich Text Strings, any formatting information will be lost");
        return new XSSFRichTextString(text);
    }

    @Override
    public RXSSFFormulaEvaluator createFormulaEvaluator() {
        return new RXSSFFormulaEvaluator(wb);
    }

    // Pass-through methods
    @Override
    public DataFormat createDataFormat() {
        return helper.createDataFormat();
    }
    @Override
    public Hyperlink createHyperlink(HyperlinkType type) {
        return helper.createHyperlink(type);
    }
    @Override
    public ExtendedColor createExtendedColor() {
        return helper.createExtendedColor();
    }
    @Override
    public ClientAnchor createClientAnchor() {
        return helper.createClientAnchor();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AreaReference createAreaReference(String reference) {
        return new AreaReference(reference, wb.getSpreadsheetVersion());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AreaReference createAreaReference(CellReference topLeft, CellReference bottomRight) {
        return new AreaReference(topLeft, bottomRight, wb.getSpreadsheetVersion());
    }

}
