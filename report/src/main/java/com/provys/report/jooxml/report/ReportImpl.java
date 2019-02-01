package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.Report;
import com.provys.report.jooxml.datasource.ReportDataSource;
import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.tplworkbook.TplWorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents JOOXML report definition.
 * While it can be modified when report definition changes, it is not modified by execution and this is reflected by
 * interface used by report executor.
 */
class ReportImpl implements Report {

    private final Map<String, ReportDataSource> dataSources;
    private ReportStep rootStep;
    private File template;

    /**
     * Initialize data sources map and verify that it is valid (single root data source, all others point to data source
     * in collection; it is supposed that data sources themselves make it sure there is no cycle)
     *
     * @param dataSources is supplied collection of data sources
     * @throws IllegalStateException is there is duplicity in internal names of supplied dtaa sources
     * @throws IllegalArgumentException if parent data source of some data source is not in supplied collection
     */
    private static Map<String, ReportDataSource> initDataSources(Collection<ReportDataSource> dataSources) {
        // collector throws exception if data source nameNms are not unique
        Map<String, ReportDataSource> result = dataSources.stream().collect(
                Collectors.toMap(ReportDataSource::getNameNm, Function.identity()));
        // verify that each data source points to other data source in collection
        dataSources.stream()
                .filter(dataSource -> (dataSource.getParent().isPresent()))
                .map(ReportDataSource::getParent).map(Optional::get)
                .filter(parent -> (parent != result.get(parent.getNameNm())))
                .findAny()
                .ifPresent(dataSource -> {
                    throw new IllegalArgumentException(
                        "Parent data source not in collection (" + dataSource.getNameNm() + ")");
                });
        return result;
    }

    /**
     * Constructor creates report based on supplied data
     *
     * @param dataSources is collection of data sources for the new report
     * @param rootRegionBuilder is builder that can be used to build root region; building is done by report constructor
     *                         as built region references items (cells etc.) in template workbook and we do not want to
     *                         reference something mutable out of our control
     * @param template is file containing template excel workbook
     */
    ReportImpl(Collection<ReportDataSource> dataSources, StepBuilder rootRegionBuilder, File template,
                      TplWorkbookFactory tplWorkbookFactory) {
        this.dataSources = initDataSources(dataSources);
        try (TplWorkbook workbook = tplWorkbookFactory.get(template)) {
            this.rootStep = rootRegionBuilder.build(Collections.unmodifiableMap(this.dataSources), workbook);
        } catch (IOException e) {
            throw new RuntimeException("IO error working with template file", e);
        }
        this.template = Objects.requireNonNull(template);
    }

    /**
     * Retrieve data source specified by internal name
     *
     * @param nameNm is internal name of data source to be retrieved
     * @return data source with given internal name
     * @throws NullPointerException is supplied internal name is null
     * @throws IllegalArgumentException in case datasource with such name doesn't exists
     */
    ReportDataSource getDataSourceByNameNm(String nameNm) {
        ReportDataSource result = dataSources.get(Objects.requireNonNull(nameNm));
        if (result == null) {
            throw new IllegalArgumentException("Data source not found by nameNm " + nameNm);
        }
        return result;
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
