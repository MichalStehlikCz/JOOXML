package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.ReportStep;
import com.provys.report.jooxml.tplworkbook.TplWorkbook;
import com.provys.report.jooxml.workbook.CellCoordinates;
import com.provys.report.jooxml.workbook.Workbooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RowCellAreaBuilder extends RowAreaBuilder<RowCellAreaBuilder> {

    private static final Logger LOG = LogManager.getLogger(RowCellAreaBuilder.class.getName());

    final private Map<CellCoordinates, CellBind> fieldBinds;

    /**
     * Default constructor of empty area.
     */
    public RowCellAreaBuilder(StepBuilder parent) {
        super(parent);
        fieldBinds = new ConcurrentHashMap<>(5);
    }

    @Override
    public String getDefaultNameNmPrefix() {
        return "CELLAREA";
    }

    @Override
    public String proposeChildName(StepBuilder child) {
        return child.getDefaultNameNmPrefix();
    }

    /**
     * @return list of value rules, applicable on area
     */
    private Map<CellCoordinates, CellBind> getFieldBinds() {
        return Collections.unmodifiableMap(fieldBinds);
    }

    /**
     * Adds value rule to region. Verifies if value rule is vlid for given region before insertion.
     *
     * @param cellBind is rule to be used for populating region
     */
    @SuppressWarnings("UnusedReturnValue")
    public RowCellAreaBuilder addFieldBind(CellBind cellBind) {
        fieldBinds.put(cellBind.getCoordinates(), cellBind);
        return self();
    }

    /**
     * Retrieves field bind for particular cell
     *
     * @param row is row index of cell being queried
     * @param col is column index of cell being queried
     * @return reference to field bind if one exists, empty optional if no bind exists for given field
     */
    public Optional<CellBind> getFieldBindAt(int row, int col) {
        return Optional.ofNullable(fieldBinds.get(Workbooks.getCellCoordinates(row, col)));
    }

    @Override
    protected RowCellAreaBuilder self() {
        return this;
    }

    private void parseBind(XMLStreamReader reader) throws XMLStreamException {
        String column = null;
        String coordinates = null;
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "COLUMN":
                        if (column != null) {
                            LOG.warn("RowCellArea: Duplicate COLUMN definition in bind");
                        }
                        column = reader.getElementText();
                        break;
                    case "CELL":
                        if (coordinates != null) {
                            LOG.warn("RowCellArea: Duplicate CELL definition in bind");
                        }
                        coordinates = reader.getElementText();
                        break;
                    default:
                        LOG.error("RowCellArea: Unsupported element {} in BIND; supported elements are CELL, COLUMN",
                                reader.getLocalName());
                        throw new RuntimeException("RowCellArea: Unsupported element " + reader.getLocalName() +
                                " in BIND; supported elements are CELL, COLUMN");
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        if (column == null) {
            LOG.warn("RowCellArea: Column missing in bind definition");
        } else if (coordinates == null) {
            LOG.warn("RowCellArea: Cell coordinates missing in bind definition");
        } else {
            addFieldBind(new CellBind(column, coordinates));
        }
    }

    RowCellAreaBuilder parse(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("BIND")) {
                    parseBind(reader);
                } else {
                    LOG.error("RowCellArea: Unsupported element {}; supported elements are BIND", reader.getLocalName());
                    throw new RuntimeException("RowCellArea: Skipping unsupported element " + reader.getLocalName() +
                            "; supported elements are BIND");
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return this;
    }

    /**
     * Verifies that specified field bind is valid for this region.
     * Generally checks if referenced cell is within region's own area and is not in one of subregion's areas.
     *
     * @param cellBind is field binding to be validated
     */
    private void validateFieldBind(CellBind cellBind) {
        Objects.requireNonNull(cellBind);
        if (cellBind.getCoordinates().getRow() < getFirstRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getFirstRow() + ", bind row " + cellBind.getCoordinates().getRow());
        }
        if (cellBind.getCoordinates().getRow() > getLastRow()) {
            throw new IllegalArgumentException("Data bind outside region validity (region first row "
                    + getLastRow() + ", bind row " + cellBind.getCoordinates().getRow());
        }
    }

    /**
     * Runs validation on all field binds in region.
     */
    private void validateFieldBinds() {
        for (CellBind cellBind : fieldBinds.values()) {
            validateFieldBind(cellBind);
        }
    }

    @Override
    public void validate() {
        super.validate();
        validateFieldBinds();
    }

    /**
     * @return collection of rows for this region
     */
    private Collection<Row> buildRows(TplWorkbook template) {
        // first we will go through cells in template and we will try to find corresponding bindings
        return Stream.concat(
                Stream.concat( // rows built from cells
                        template.getSheet()// cells retrieved from template
                                .getRows(getFirstRow(), getLastRow())
                                .stream()
                                .flatMap(row -> row.getCells().stream())
                                .filter(cell -> this.isInTemplateRegion(cell.getRowIndex(), cell.getColIndex()))
                                .map(cell -> new CellBuilder(cell, this))
                        , getFieldBinds().values().stream() // cells construed from required field binds
                                .map(cellBind -> new CellBuilder(cellBind, this))
                ).collect(Collectors.groupingBy(CellBuilder::getRowIndex
                        , new RowBuilder.RowBuilderCollector())).values().stream()
                , template.getSheet() // rows built from template (no cells, but row height etc. )
                        .getRows(getFirstRow(), getLastRow())
                        .stream()
                        .map(row -> new RowBuilder(row, this))
        ).collect(Collectors.groupingBy(RowBuilder::getRowIndex, new RowBuilder.AreaRowCollector())).values();
    }

    @Override
    protected ReportStep doBuild(TplWorkbook template) {
        return new RowCellArea(getNameNm().orElseThrow() /* empty should be handled by validation */, isTopLevel(),
                getLastRow() - getFirstRow() + 1, buildRows(template));
    }
}
