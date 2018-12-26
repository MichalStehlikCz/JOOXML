package com.provys.jooxml.repexecutor;

import java.util.*;

/**
 * Describes report region and enables its usage for pushing data to target worksheet.
 * Region can reference parent's dataset or subdataset. In case it has its own dataset, it iterates through records
 * returned by this dataset; if it references parent's dataset, it will just use current record from this dataset.
 * Region either uses its own cells, or defers rendering to subregions.
 */
public interface ReportRegion extends Iterable<ReportSubRegion> {

    /**
     * @return internal name of given region, used as region identification, must be unique between children of given
     * parent
     */
    String getNameNm();

    /**
     * @return DataSource this region is based on, empty optional if region uses parent region's DataSource
     */
    public Optional<ReportDataSource> getDataSource();
}
