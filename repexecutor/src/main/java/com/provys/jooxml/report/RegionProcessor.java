package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.*;
import org.apache.poi.ss.util.CellReference;

import java.util.*;

/**
 * Object gets parent processor (to evaluate links) and data context, region definition and worksheet and applies
 * region on worksheet
 */
public class RegionProcessor<T extends RowRegion> {
    private final T region;
    /**
     * Nested list is sufficient as we always process cells row by row and then all cells in row, regardless of order
     */
    private final List<Row> rows;

    /**
     * Find all cells that fall into specified region.
     *
     * @param region defines area in excel sheet where we should look for cells
     * @param template is template excel sheet
     * @return list of cells and their associated bindings
     */
    private static List<Row> initCellsInRegion(ReportRegion region, TemplateWorkbook template) {
        List<Row> result = new ArrayList<>(region.getLastTemplateRow() - region.getFirstTemplateRow() + 1);
        Set<CellReference> usedBinds = new HashSet<> (region.getFieldBinds().size());
        // first we will go through cells in template and we will try to find corresponding bindings
        for (TemplateRow templateRow : template.getSheet().getRows(region.getFirstTemplateRow(), region.getLastTemplateRow())) {
            Row row = new Row(templateRow.getRowNum(), templateRow.getPhysicalNumberOfCells());
            for (TemplateCell templateCell : templateRow) {
                CellWithBind newCell = new TemplateCellWithBind(templateCell, region);
                row.add(newCell);
                newCell.getFieldBind().ifPresent(fieldBind -> usedBinds.add(fieldBind.getCellReference()));
            }
        }
        // then we go through all bindings and if binding is not used, we add an empty cell to use it on
        region.getFieldBinds().values().stream()
                .filter(fieldBind -> (!usedBinds.contains(fieldBind.getCellReference())))
                .map(fieldBind -> new EmptyCellWithBind(fieldBind))
                .forEach();
    }

    public RegionProcessor(T region, TemplateWorkbook template) {
        this.region = region;
        this.rows = initCellsInRegion(region, template);
    }

    private static class Row extends ArrayList<CellWithBind> {
        int rownum;

        Row(int rownum, int initialCapacity) {
            super(initialCapacity);
            this.rownum = rownum;
        }

        int getRownum() {
            return rownum;
        }
    }
}
