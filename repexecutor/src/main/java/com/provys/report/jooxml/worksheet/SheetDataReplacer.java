package com.provys.report.jooxml.worksheet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class SheetDataReplacer extends InputStream {
    private static final Logger LOG = LogManager.getLogger(SheetDataReplacer.class.getName());
    final InputStream inputStream;
    final InputStream worksheetData;
    boolean dataActive = false; // indicates that data from worksheetData are actually being processed
    boolean waitingForSheetData = true; // indicates that we still look for SheetData section
    final byte[] sheetDataPattern = "<sheetData".getBytes(StandardCharsets.UTF_8);
    int patternPosition = 0;
    boolean patternFound = false; // signalisation that we matched pattern and should verify end of pattern
    boolean endSheetDataActive = false;
    final byte[] endSheetData = "</sheetData>".getBytes(StandardCharsets.UTF_8);
    int endSheetDataPosition = 0;

    public SheetDataReplacer(InputStream inputStream, InputStream worksheetData) {
        this.inputStream = inputStream;
        this.worksheetData = worksheetData;
    }

    public int read() throws IOException {
        // read from worksheet data if it is active
        if (dataActive) {
            int c = worksheetData.read();
            if (c > -1) {
                return c;
            }
            dataActive = false;
            // reached end of input data - need to write end tag for sheetData
            endSheetDataActive = true;
            endSheetDataPosition = 0;
        }
        // writing sheet data end tag
        if (endSheetDataActive) {
            if (endSheetDataPosition < endSheetData.length) {
                return endSheetData[endSheetDataPosition++];
            }
            endSheetDataActive = false;
        }
        // matched pattern, verify proper end of tag
        if (patternFound) {
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
                        }
                    }
                    if (! endSheetDataFound) {
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
        // input from source document active
        int c = inputStream.read();
        if (c == -1) {
            return -1;
        }
        if (waitingForSheetData) {
            // parsing
            if (c == sheetDataPattern[patternPosition]) {
                patternPosition++;
                if (patternPosition == sheetDataPattern.length) {
                    patternFound = true;
                }
            }
        }
        return c;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        worksheetData.close();
    }
}
