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

import java.util.Iterator;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.ObjectData;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;

/**
 * Streaming version of Drawing.
 * Delegates most tasks to the non-streaming XSSF code.
 * TODO: Potentially, Comment and Chart need a similar streaming wrapper like Picture.
 */
public class RXSSFDrawing implements Drawing<XSSFShape> {
    private final RXSSFWorkbook _wb;
    private final XSSFDrawing _drawing;

    public RXSSFDrawing(RXSSFWorkbook workbook, XSSFDrawing drawing) {
        this._wb = workbook;
        this._drawing = drawing;
    }

    @Override
    public RXSSFPicture createPicture(ClientAnchor anchor, int pictureIndex) {
        XSSFPicture pict = _drawing.createPicture(anchor, pictureIndex);
        return new RXSSFPicture(_wb, pict);
    }

    @Override
    public Comment createCellComment(ClientAnchor anchor) {
        return _drawing.createCellComment(anchor);
    }

    @Override
    public ClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2) {
        return _drawing.createAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
    }

    @Override
    public ObjectData createObjectData(ClientAnchor anchor, int storageId, int pictureIndex) {
        return _drawing.createObjectData(anchor, storageId, pictureIndex);
    }

    @Override
    public Iterator<XSSFShape> iterator() {
        return _drawing.getShapes().iterator();
    }
    
}

