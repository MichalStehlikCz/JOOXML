package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.repworkbook.RepWorkbook;

/**
 * RegionProcessor is worker able to process given report region and push its lines to resulting worksheet. Processing
 * should use following sequence of steps
 * - processor is created (constructor) ; it immediatelly creates processors for individual records or subregions
 * - processor is opened - it might prepare statements for its associated datasource (and only do rebinding on
 * individual rows)
 * - processor is called to process rows for given parent node
 * - processor is closed - it closes prepared statements and other resources
 *
 * Processor holds data context
 */
public interface RegionProcessor extends AutoCloseable {
    ExecRegion getExecRegion();

    /**
     * Initialize data context
     */
    void open();

    /**
     * Process given region and publish its data to worksheet
     */
    void process(RepWorkbook workbook);
}
