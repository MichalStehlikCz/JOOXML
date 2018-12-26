package com.provys.jooxml.repexecutor;

import java.io.File;

/**
 * Interface to report - immutable data structure representing report definition. Report consists of immutable regions,
 * that are already in form optimal for report execution
 */
public interface Report {
    /**
     * @return root region
     */
    ReportRegion getRootRegion();

    /**
     * @return template workbook this report is based on
     */
    File getTemplate();
}
