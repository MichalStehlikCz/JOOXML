package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CellValueBoolean extends CellValueBase<Boolean> {

    private static final CellValueBoolean TRUE = new CellValueBoolean(Boolean.TRUE);
    private static final CellValueBoolean FALSE = new CellValueBoolean(Boolean.FALSE);
    private static final CellValueBoolean EMPTY = new CellValueBoolean(null);

    /**
     * Creates new cell value of boolean type from supplied value.
     *
     * @param value is value to be used for given cell
     * @return boolean cell value with given value
     */
    @Nonnull
    public static CellValueBoolean of(@Nullable Boolean value) {
        if (value == null) {
            return EMPTY;
        } else if (value) {
            return TRUE;
        }
      return FALSE;
    }

    private CellValueBoolean(@Nullable Boolean value) {
        super(value);
    }

    @Override
    @Nonnull
    public CellType getCellType() {
        return CellType.BOOLEAN;
    }

}
