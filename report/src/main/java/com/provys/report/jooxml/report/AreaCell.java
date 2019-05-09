package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.repexecutor.AreaCellPath;
import com.provys.report.jooxml.repexecutor.CellPathReplacer;
import com.provys.report.jooxml.repexecutor.ExecRegionContext;
import com.provys.report.jooxml.workbook.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface AreaCell {

    /**
     * @return column index; columns are indexed from 0
     */
    int getColIndex();

    /**
     * @return get type of cell (string, numeric, boolean, formula)
     */
    @Nonnull
    CellType getCellType();

    /**
     * @return value present in given cell
     */
    @Nonnull
    Optional<CellValue> getCellValue();

    /**
     * Get effective value of given cell. If column binding is present in cell, retrieve value from supplied data.
     * Otherwise, use cell value. For formula cell, replace cell references with encoded paths
     *
     * @param data is data record used to evaluate value from binding
     * @return value of given cell
     */
    @Nonnull
    CellValue getEffectiveValue(DataRecord data, ExecRegionContext execRegionContext, CellPathReplacer cellPathReplacer);

    /**
     * @return properties associated with given cell
     */
    @Nonnull
    Optional<CellProperties> getProperties();

    /**
     * @return source field if cell has data binding
     */
    @Nonnull
    Optional<String> getBindColumn();

    /**
     * @return map of cell references in formula and their corresponding area paths. Returns empty list for empty
     * cells or cells of any other type than formula
     */
    @Nonnull
    public Map<String, AreaCellPath> getReferenceMap();
}
