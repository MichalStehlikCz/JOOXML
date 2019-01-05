package com.provys.report.jooxml.report;

import com.provys.report.jooxml.datasource.DataRecord;
import com.provys.report.jooxml.workbook.CellProperties;
import com.provys.report.jooxml.workbook.CellType;
import com.provys.report.jooxml.workbook.CellValue;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents cell created because of binding. Cell was not present in template sheet, but there is data binding
 * for this cell in region description, thus we consider this cell to be String cell without any special formatting
 */
class EmptyCellWithBind implements AreaCell {

    final private int columnIndex;
    final String bindColumn; // in fact, this field is always filled in; but getter needs Optional to be compatible
                                 // with other region cell types, thus it is more effective to store value as optional
                                 // even in this case

    /**
     * Constructor used when bind points to empty (non-existent) cell.
     *
     * @param columnIndex is column coordinate of new cell
     * @param bindColumn is name of bind column to supply value
     */
    EmptyCellWithBind(int columnIndex, String bindColumn) {
        this.columnIndex = columnIndex;
        this.bindColumn = Objects.requireNonNull(bindColumn);
    }

    @Override
    public int getColIndex() {
        return columnIndex;
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    public Optional<CellValue> getCellValue() {
        return Optional.empty();
    }

    @Override
    public CellValue getEffectiveValue(DataRecord data) {
        return data.getValue(bindColumn);
    }

    @Override
    public Optional<CellProperties> getProperties() {
        return Optional.empty();
    }

    /**
     * Returns Optional to fulfill interface contract, even though bind is always present for this class
     *
     * @return source field for cell's data binding
     */
    @Override
    public Optional<String> getBindColumn() {
        return Optional.of(bindColumn);
    }
}
