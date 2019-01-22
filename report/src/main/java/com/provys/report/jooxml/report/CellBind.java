package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents rule for population of single field in sheet from source dataset.
 */
final class CellBind {

    private static final Pattern columnValidator = Pattern.compile("[a-zA-Z][a-zA-Z0-9_$]");
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
        if (!columnValidator.matcher(Objects.requireNonNull(sourceColumn)).matches()) {
            throw new IllegalArgumentException("Column name must start with letter and contain opnly letters, numbers " +
                    "and _, not " + sourceColumn);
        }
        this.sourceColumn = sourceColumn;
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates CellBind from supplied data.
     *
     * @param sourceColumn is name of column in dataset used to populate cell
     * @param address is string reference to cell in Excel format
     */
    CellBind(String sourceColumn, String address) {
        this(sourceColumn, CellCoordinates.parse(address));
    }

    @Nonnull
    String getSourceColumn() {
        return sourceColumn;
    }

    @Nonnull
    CellCoordinates getCoordinates() {
        return coordinates;
    }
}
