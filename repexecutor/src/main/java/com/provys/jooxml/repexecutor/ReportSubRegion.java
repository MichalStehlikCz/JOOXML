package com.provys.jooxml.repexecutor;

/**
 * ReportSubRegion interface adds offset against parent or previous region to ReportRegion interface
 */
public interface ReportSubRegion extends ReportRegion {

    /**
     * @return offset against parent / previous subregion
     */
    int getOffset();
}
