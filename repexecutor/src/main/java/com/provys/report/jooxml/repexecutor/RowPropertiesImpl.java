package com.provys.report.jooxml.repexecutor;

class RowPropertiesImpl implements RowProperties {

    private final float heightInPoints;
    private final boolean hidden;
    private final int styleIndex;

    RowPropertiesImpl(float heightInPoints, boolean hidden, int styleIndex) {
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
}
