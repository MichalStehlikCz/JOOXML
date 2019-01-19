package com.provys.report.jooxml.workbook.impl;

import com.provys.report.jooxml.workbook.RowProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class RowPropertiesInt implements RowProperties {

    @Nullable
    private final Float heightInPoints;
    private final boolean hidden;
    @Nullable
    private final Short styleIndex;

    @Nonnull
    public static RowPropertiesInt of(@Nullable Float heightInPoints, boolean hidden, @Nullable Short styleIndex) {
        return new RowPropertiesInt(heightInPoints, hidden, styleIndex);
    }

    private RowPropertiesInt(@Nullable Float heightInPoints, boolean hidden, @Nullable Short styleIndex) {
        if ((heightInPoints != null) && (heightInPoints.compareTo(0f) < 0)) {
            throw new IllegalArgumentException("Row height cannot be negative");
        }
        this.heightInPoints = heightInPoints;
        this.hidden = hidden;
        if ((styleIndex != null) && (styleIndex < (short) 0)) {
            throw new IllegalArgumentException("Style index cannot be negative");
        }
        this.styleIndex = styleIndex;
    }

    @Override
    @Nonnull
    public Optional<Float> getHeightInPoints() {
        return Optional.ofNullable(heightInPoints);
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    @Nonnull
    public Optional<Short> getStyleIndex() {
        return Optional.ofNullable(styleIndex);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowPropertiesInt that = (RowPropertiesInt) o;

        if (!getHeightInPoints().equals(that.getHeightInPoints())) return false;
        if (isHidden() != that.isHidden()) return false;
        return getStyleIndex().equals(that.getStyleIndex());
    }

    @Override
    public int hashCode() {
        int result = getHeightInPoints().hashCode();
        result = 31 * result + (isHidden() ? 1 : 0);
        result = 31 * result + getStyleIndex().hashCode();
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        return "RowPropertiesInt{" +
                "heightInPoints=" + getHeightInPoints().map(Object::toString).orElse("null") +
                ", hidden=" + isHidden() +
                ", styleIndex=" + getStyleIndex().map(Object::toString).orElse("null") +
                '}';
    }
}
