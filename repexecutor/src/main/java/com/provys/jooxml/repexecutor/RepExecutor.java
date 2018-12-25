package com.provys.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.provys.jooxml.worksheet.RXSSWorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private Report report;
    private File targetFile;
    final private RepWorkbookFactory repWorkBookFactory = new RXSSWorkbookFactory();

    public RepExecutor(Report report, File targetFile) {
        this.report = report;
        this.targetFile = targetFile;
    }

    private RepWorkbook readWorkbook() {
        try {
            LOG.info("ReadWorkbook: Read template workbook from file {}", report.getTemplate());
            return repWorkBookFactory.get(report.getTemplate());
        } catch (IOException ex) {
            LOG.error("ReadWorkbook: UI error reading workbook {} {}", report.getTemplate(), ex);
            throw new RuntimeException("IO error reading workbook", ex);
        } catch (InvalidFormatException ex) {
            LOG.error("ReadWorkbook: Invalid format exception reading {} {}", report.getTemplate(), ex);
            throw new RuntimeException("Format error reading workbook", ex);
        }
    }

    private void writeWorkbook(RepWorkbook workbook) {
        LOG.info("WriteWorkbook: Write workbook to file {}", targetFile);
        try (OutputStream stream = Files.newOutputStream(targetFile.toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            workbook.write(stream);
        } catch (IOException ex) {
            LOG.error("WriteWorkbook: IO error writing workbook {} {}", targetFile, ex);
            throw new RuntimeException("IO error writing workbook", ex);
        }
    }

    public void Run() {
        try (RepWorkbook workbook = readWorkbook()) {
            writeWorkbook(workbook);
        } catch (IOException ex) {
            LOG.error("Workbook: IO error closing workbook {}", ex);
            throw new RuntimeException("IO error closing workbook", ex);
        }
    }
}
