package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.RowProperties;

import javax.annotation.Nonnull;

public class RowPropertiesInt implements RowProperties {

    private final float heightInPoints;
    private final boolean hidden;
    private final int styleIndex;

    @Nonnull
    static public RowPropertiesInt of(float heightInPoints, boolean hidden, int styleIndex) {
        return new RowPropertiesInt(heightInPoints, hidden, styleIndex);
    }

    private RowPropertiesInt(float heightInPoints, boolean hidden, int styleIndex) {
        this.heightInPoints = heightInPoints;
        this.hidden = hidden;
        this.styleIndex = styleIndex;
    }

    @Override
    public float getHeightInPoints() {
        return heightInPoints;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public int getStyleIndex() {
        return styleIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowPropertiesInt that = (RowPropertiesInt) o;

        if (Float.compare(that.getHeightInPoints(), getHeightInPoints()) != 0) return false;
        if (isHidden() != that.isHidden()) return false;
        return getStyleIndex() == that.getStyleIndex();
    }

    @Override
    public int hashCode() {
        int result = (getHeightInPoints() != +0.0f ? Float.floatToIntBits(getHeightInPoints()) : 0);
        result = 31 * result + (isHidden() ? 1 : 0);
        result = 31 * result + getStyleIndex();
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        return "RowPropertiesInt{" +
                "heightInPoints=" + heightInPoints +
                ", hidden=" + hidden +
                ", styleIndex=" + styleIndex +
                '}';
    }
}
