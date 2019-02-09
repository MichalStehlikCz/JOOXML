package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
final class CellBind {

    @Nonnull
    private static final Pattern columnValidator = Pattern.compile("[a-zA-Z][a-zA-Z0-9_$]*");

    /**
     * Validate supplied String as column name and return uppercase version of it
     *
     * @param sourceColumn is value to be validated
     * @return validated (uppercase) version of supplied string
     * @throws IllegalArgumentException if string does not pass validation
     */
    @Nonnull
    static String validateSourceColumn(String sourceColumn) {
        if (!columnValidator.matcher(Objects.requireNonNull(sourceColumn)).matches()) {
            throw new IllegalArgumentException("Column name must start with letter and contain only letters, numbers " +
                    "and _, not " + sourceColumn);
        }
        return sourceColumn.toUpperCase();
    }

    @Nonnull
    private final String sourceColumn;
    @Nonnull
    private final CellCoordinates coordinates;

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param coordinates is reference to cell as used in internal representation in POI spreadsheets
     * @throws IllegalArgumentException if column name does not match pattern (first letter, then letters, numbers and
     * _ or $ characters)
     */
    CellBind(String sourceColumn, CellCoordinates coordinates) {
        this.sourceColumn = validateSourceColumn(sourceColumn);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    @Nonnull
    String getSourceColumn() {
        return sourceColumn;
    }

    @Nonnull
    CellCoordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellBind cellBind = (CellBind) o;
        if (!getSourceColumn().equals(cellBind.getSourceColumn())) return false;
        return getCoordinates().equals(cellBind.getCoordinates());
    }

    @Override
    public int hashCode() {
        int result = getSourceColumn().hashCode();
        result = 31 * result + getCoordinates().hashCode();
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        return "CellBind{" +
                "sourceColumn='" + sourceColumn + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

}
