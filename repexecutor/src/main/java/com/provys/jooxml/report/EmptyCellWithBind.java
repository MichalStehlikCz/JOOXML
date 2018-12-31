package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.DataRecord;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Optional;

/**
 * Represents cell created because of binding. Cell was not present in template sheet, but there is data binding
 * for this cell in region description, thus we consider this cell to be String cell without any special formatting
 */
class EmptyCellWithBind implements AreaCell {

    final private int columnIndex;
    final Optional<String> bindColumn; // in fact, this field is always filled in; but getter needs Optional to be compatible
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
        this.bindColumn = Optional.of(bindColumn);
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    public String getCellFormula() {
        throw new RuntimeException("Cannot access formula in empty template cell");
    }

    @Override
    public String getStringCellValue() {
        return null;
    }

    @Override
    public double getNumericCellValue() {
        throw new RuntimeException("Cannot access numeric value in empty template cell");
    }

    @Override
    public boolean getBooleanCellValue() {
        throw new RuntimeException("Cannot access boolean value in empty template cell");
    }

    @Override
    public byte getErrorCellValue() {
        throw new RuntimeException("Cannot access error value in empty template cell");
    }

    @Override
    public String getStringEffectiveValue(DataRecord data) {
        return data.getStringValue(bindColumn.get());
    }

    @Override
    public double getNumericEffectiveValue(DataRecord data) {
        throw new RuntimeException("Cannot access numeric value in empty template cell");
    }

    @Override
    public boolean getBooleanEffectiveValue(DataRecord data) {
        throw new RuntimeException("Cannot access error value in empty template cell");
    }

    @Override
    public int getCellStyleIndex() {
        return -1;
    }

    @Override
    public Comment getCellComment() {
        return null;
    }

    @Override
    public Hyperlink getHyperlink() {
        return null;
    }

    /**
     * Returns Optional to fulfill interface contract, even though bind is always present for this class
     *
     * @return source field for cell's data binding
     */
    @Override
    public Optional<String> getBindColumn() {
        return bindColumn;
    }
}
