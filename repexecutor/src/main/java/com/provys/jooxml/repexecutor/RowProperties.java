package com.provys.jooxml.repexecutor;

public interface RowProperties {

    /**
     * Return new instance of RowProperties with given characteristics
     *
     * @param heightInPoints is height in points for given row, -1 if left on default height
     * @param hidden indicates if row should be hidden
     * @param styleIndex is style index (within given workbook), -1 if no style is set
     * @return new instance of RowProperties
     */
    static RowProperties get(float heightInPoints, boolean hidden, int styleIndex) {
        return new RowPropertiesImpl(heightInPoints, hidden, styleIndex);
    }

    /**
     * @return row height in points, -1 if row does not have explciitly set height
     */
    float getHeightInPoints();

    /**
     * @return if row is hidden
     */
    boolean isHidden();

    /**
     * @return row cell style index; we do not manipulate with styles, thus it is possible to transfer style from
     * template to target sheet using index
     */
    int getStyleIndex();
}
