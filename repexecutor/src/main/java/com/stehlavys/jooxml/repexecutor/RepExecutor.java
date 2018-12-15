package com.stehlavys.jooxml.repexecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class RepExecutor {

    private static final Logger LOG = LogManager.getLogger(RepExecutor.class.getName());
    private File template;
    private File destFile;

    public RepExecutor(String templateFileName, String destFileName) {
        this.template = new File(templateFileName);
        this.destFile = new File(destFileName);
    }

    private void initDestFile() {
        try {
            Files.copy(template.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOG.info("InitDestFile: copied template to dest {} {}", template, destFile);
        } catch (IOException ex) {
            LOG.error("InitDestFile: Error copying template to target {} {} {}", template, destFile, ex);
            throw new RuntimeException("Error copying template to target", ex);
        }
    }

    private RepWorkbook readWorkbook() {
        try {
            LOG.info("ReadWorkbook: Read workbook from file {}", template);
            return new RepWorkbook(template);
        } catch (IOException ex) {
            LOG.error("ReadWorkbook: UI error reading workbook {} {}", template, ex);
            throw new RuntimeException("IO error reading workbook", ex);
        } catch (InvalidFormatException ex) {
            LOG.error("ReadWorkbook: Invalid format exception reading {} {}", template, ex);
            throw new RuntimeException("Format error reading workbook", ex);
        }
    }

    private void writeWorkbook(RepWorkbook workbook) {
        LOG.info("WriteWorkbook: Write workbook to file {}", template);
        try (OutputStream stream = Files.newOutputStream(destFile.toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            workbook.write(stream);
        } catch (IOException ex) {
            LOG.error("WriteWorkbook: IO error writing workbook {} {}", destFile, ex);
            throw new RuntimeException("IO error writing workbook", ex);
        }
    }

    public void Run() {
        try (RepWorkbook workbook = readWorkbook()) {
            writeWorkbook(workbook);
        }
    }
}
