package com.provys.report.jooxml.repworkbook.impl;

import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Class implements replacing cell references in formulas that are encoded by CellPathReplacer to normal cell
 * references. It is applied on worksheet data input stream.
 */
class FormulaCellReferenceReplacer extends InputStream {

    private static final Logger LOG = LogManager.getLogger(SheetDataReplacer.class.getName());

    @Nonnull
    private final InputStream inputStream;
    @Nonnull
    private final CellPathReplacer cellPathReplacer;
    @Nonnull
    private final ExecRegion execRegion;

    /**
     * Pattern used to match start of formula definition. At the moment, we expect that formula value is the only
     * {@code <f>} tag occurence and that this tag has no attributes
     */
    private static final byte[] FORMULA_PATTERN = "<f>".getBytes(StandardCharsets.UTF_8);
    /**
     * Current position in matching FORMULA_PATTERN pattern. Zero when no characters have been matched yet, incremented
     * if pattern is being read from input stream
     */
    private int patternPosition = 0;
    /**
     * Flag indicates that formula is being processed.
     * Flag is set in activateFormula when FORMULA_PATTERN has been found and reset when whole formula is read in
     * readFormula
     */
    private boolean formulaActive = false;
    /**
     * Formula being read; only relevant when formulaActive is true
     */
    private String formula;
    /**
     * Position in formula; only relevant when formulaActive is true
     */
    private int formulaPosition;
    private static final byte[] END_FORMULA_PATTERN = "</f>".getBytes(StandardCharsets.UTF_8);
    private boolean endFormulaActive = false;
    private int endFormulaPosition = 0;

    /**
     * @param inputStream is input stream containing sheetData section (rows and columns)
     */
    FormulaCellReferenceReplacer(InputStream inputStream, CellPathReplacer cellPathReplacer, ExecRegion execRegion) {
        this.inputStream = Objects.requireNonNull(inputStream);
        this.cellPathReplacer = Objects.requireNonNull(cellPathReplacer);
        this.execRegion = Objects.requireNonNull(execRegion);
    }

    private void activateFormula() throws IOException {
        // we have <f> - read formula
        var builder = new StringBuilder();
        // we want to skip sheet data section
        boolean endFormulaFound = false;
        int c;
        endFormulaPosition = 0;
        while ((c = inputStream.read()) != -1) {
            builder.append(Character.toString(c));
            if (c == END_FORMULA_PATTERN[endFormulaPosition]) {
                endFormulaPosition++;
                if (endFormulaPosition == END_FORMULA_PATTERN.length) {
                    endFormulaFound = true;
                    break;
                }
            } else {
                endFormulaPosition = 0;
            }
        }
        if (!endFormulaFound) {
            LOG.error("WriteWorkbook: </f> not found");
            throw new RuntimeException("</f> not found");
        }
        formula = cellPathReplacer.decode(builder.substring(0, builder.length() - END_FORMULA_PATTERN.length),
                execRegion);
        formulaActive = true;
        formulaPosition = 0;
    }

    private int readFormula() {
        var result = formula.charAt(formulaPosition++);
        if (formulaPosition >= formula.length()) {
            formulaActive = false;
            endFormulaActive = true;
            endFormulaPosition = 0;
        }
        return result;
    }

    /**
     * Read character from end formula tag if it is active
     *
     * @return character read if data were read successfully, -1 if end was reached
     */
    private int readEndFormula() {
        var result = END_FORMULA_PATTERN[endFormulaPosition++];
        if (endFormulaPosition >= END_FORMULA_PATTERN.length) {
            endFormulaActive = false;
        }
        return result;
    }

    public int read() throws IOException {
        // read data from worksheet if it is active
        if (formulaActive) {
            return readFormula();
        }
        // read sheet data end tag
        if (endFormulaActive) {
            return readEndFormula();
        }
        // input from source document
        var character = inputStream.read();
        if (character == -1) {
            return -1;
        }
        if (character == FORMULA_PATTERN[patternPosition++]) {
            if (patternPosition == FORMULA_PATTERN.length) {
                activateFormula();
                patternPosition = 0;
            }
        } else {
            patternPosition = 0;
        }
        return character;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
