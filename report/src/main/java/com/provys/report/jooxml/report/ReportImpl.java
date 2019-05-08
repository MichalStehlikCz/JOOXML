package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents JOOXML report definition.
 * While it can be modified when report definition changes, it is not modified by execution and this is reflected by
 * interface used by report executor.
 */
class ReportImpl implements Report {

    private final Map<String, ReportDataSource> dataSources;
    private final ReportStep rootStep;
    private File template;

    private void addDataSource(ReportDataSource dataSource) {
        if (dataSources.putIfAbsent(dataSource.getNameNm(), dataSource) != null) {
            throw new RuntimeException("Duplicate internal name of report data source in report");
        }
        for (var child : dataSource.getChildren()) {
            addDataSource(child);
        }
    }

    /**
     * Constructor creates report based on supplied data
     *
     * @param rootDataSource is root data source for the new report
     * @param rootRegionBuilder is builder that can be used to build root region; building is done by report constructor
     *                         as built region references items (cells etc.) in template workbook and we do not want to
     *                         reference something mutable out of our control
     * @param template is file containing template excel workbook
     */
    ReportImpl(ReportDataSource rootDataSource, StepBuilder rootRegionBuilder, File template,
                      TplWorkbookFactory tplWorkbookFactory) {
        if (rootDataSource.getParent().isPresent()) {
            throw new RuntimeException("Root data source cannot have parent");
        }
        this.dataSources = new HashMap<>(5);
        addDataSource(rootDataSource);
        try (TplWorkbook workbook = tplWorkbookFactory.get(template)) {
            this.rootStep = rootRegionBuilder.build(Collections.unmodifiableMap(this.dataSources), workbook);
        } catch (IOException e) {
            throw new RuntimeException("IO error working with template file", e);
        }
        this.template = Objects.requireNonNull(template);
    }

    /**
     * @return root region
     */
    @Override
    public ReportStep getRootStep() {
        return rootStep;
    }

    /**
     * @return template this report is based on
     */
    @Override
    public File getTemplate() {
        return template;
    }
}
