package com.provys.report.jooxml.repworkbook.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class SheetDataReplacer extends InputStream {
    private static final Logger LOG = LogManager.getLogger(SheetDataReplacer.class.getName());
    private final InputStream inputStream;
    private final InputStream worksheetData;
    private boolean dataActive = false; // indicates that data from worksheetData are actually being processed
    private boolean waitingForSheetData = true; // indicates that we still look for SheetData section
    private final byte[] sheetDataPattern = "<sheetData".getBytes(StandardCharsets.UTF_8);
    private int patternPosition = 0;
    private boolean patternFound = false; // signalisation that we matched pattern and should verify end of pattern
    private boolean endSheetDataActive = false;
    private final byte[] endSheetData = "</sheetData>".getBytes(StandardCharsets.UTF_8);
    private int endSheetDataPosition = 0;

    SheetDataReplacer(InputStream inputStream, InputStream worksheetData) {
        this.inputStream = inputStream;
        this.worksheetData = worksheetData;
    }

    /**
     * Read character from worksheet if it is active.
     *
     * @return character read if data were read successfully, -1 if worksheet is not active or end of worksheet has
     * been reached
     */
    private int readSheetData() throws IOException {
        int c = worksheetData.read();
        if (c > -1) {
            return c;
        }
        dataActive = false;
        // reached end of input data - need to write end tag for sheetData
        endSheetDataActive = true;
        endSheetDataPosition = 0;
        return -1;
    }

    /**
     * Read character from end sheet tag if it is active
     *
     * @return character read if data were read successfully, -1 if end sheet tag is not active or end was reached
     */
    private int readEndSheetData() {
        if (endSheetDataPosition < endSheetData.length) {
            return endSheetData[endSheetDataPosition++];
        }
        endSheetDataActive = false;
        return -1;
    }

    private int readStartSheetTag() throws IOException {
        patternFound = false;
        // we have <sheetData - find if tag is closed
        int c = inputStream.read();
        switch (c) {
            case -1:
                LOG.error("WriteWorkbook: EOF after <sheetData");
                throw new RuntimeException("EOF after <sheetData");
            case '/':
                // empty sheet data section
                c = inputStream.read();
                if (c != '>') {
                    LOG.error("WriteWorkbook: > expected after <sheetData/");
                    throw new RuntimeException("> expected after <sheetData/");
                }
                break;
            case '>':
                // we want to skip sheet data section
                boolean endSheetDataFound = false;
                while ((c = inputStream.read()) != -1) {
                    if (c == endSheetData[endSheetDataPosition]) {
                        endSheetDataPosition++;
                        if (endSheetDataPosition == endSheetData.length) {
                            endSheetDataFound = true;
                            break;
                        }
                    } else {
                        endSheetDataPosition = 0;
                    }
                }
                if (!endSheetDataFound) {
                    LOG.error("WriteWorkbook: </sheetData> not found");
                    throw new RuntimeException("</sheetData> not found");
                }
                break;
            default:
                // no end of element -> probably other element, reset pattern search
                patternPosition = 0;
                return c;
        }
        // have to write > to sheetData element and activate worksheetData stream
        waitingForSheetData = false;
        dataActive = true;
        return '>';
    }

    public int read() throws IOException {
        // read data from worksheet if it is active
        var character = -1;
        if (dataActive) {
            character = readSheetData();
        }
        // read sheet data end tag
        if ((character == -1) && (endSheetDataActive)) {
            character = readEndSheetData();
        }
        // matched pattern, verify proper end of tag and write >
        if ((character == -1) && (patternFound)) {
            character = readStartSheetTag();
        }
        // input from source document active
        if (character == -1) {
            character = inputStream.read();
            if (character == -1) {
                return -1;
            }
            if ((waitingForSheetData) && (character == sheetDataPattern[patternPosition])) {
                patternPosition++;
                if (patternPosition == sheetDataPattern.length) {
                    patternFound = true;
                }
            } else {
                patternPosition = 0;
            }
        }
        return character;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        worksheetData.close();
    }
}
