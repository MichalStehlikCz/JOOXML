package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents JOOXML report definition.
 * While it can be modified when report definition changes, it is not modified by execution and this is reflected by
 * interface used by report executor.
 */
public class ReportImpl implements com.provys.jooxml.repexecutor.Report {
    private final Map<String, ReportDataSource> dataSources;
    private ReportRegion rootRegion;
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
                Collectors.toMap(ReportDataSource::getNameNm, dataSource -> dataSource));
        // verify that each data source points to other data source in collection
        dataSources.stream()
                .filter(dataSource -> (dataSource.getParent() != null))
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
     * Validate that root region points to root data source of this report
     *
     * @param rootRegion is region being validated
     * @param dataSources is map of data sources belonging to this report
     * @return root region if everything goes ok
     * @throws IllegalArgumentException if root region is not connected to root data source
     * @throws IllegalArgumentException is root region's data source is not found between supplied data sources
     */
    private static ReportRegion validateRootRegion(ReportRegion rootRegion, Map<String, ReportDataSource> dataSources) {
        ReportDataSource dataSource = rootRegion.getReportDataSource();
        if (dataSource.getParent() != null) {
            throw new IllegalArgumentException("Root region must be connected to root data source");
        }
        if (dataSource != dataSources.get(dataSource.getNameNm())) {
            throw new IllegalArgumentException("Root region must be connected to data source from the same report");
        }
        return rootRegion;
    }

    /**
     * Constructor creates report based on supplied data
     *
     * @param dataSources is collection of data sources for the new report
     * @param rootRegion is root region, one that should be used with root data source
     * @param template is file containing template excel workbook
     */
    public ReportImpl(Collection<ReportDataSource> dataSources, ReportRegion rootRegion, File template) {
        this.dataSources = initDataSources(dataSources);
        this.rootRegion = validateRootRegion(rootRegion, this.dataSources);
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
    public ReportDataSource getDataSourceByNameNm(String nameNm) {
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
    public ReportRegion getRootRegion() {
        return rootRegion;
    }

    /**
     * @return template this report is based on
     */
    @Override
    public File getTemplate() {
        return template;
    }
}
