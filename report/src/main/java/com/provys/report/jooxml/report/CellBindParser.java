package com.provys.report.jooxml.report;

import com.provys.report.jooxml.workbook.CellCoordinatesFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Class supports parsing of CellBind from XML file.
 */
@Singleton
class CellBindParser {

    @Nonnull
    private static final String COLUMN_TAG = "COLUMN";
    @Nonnull
    private static final String CELL_TAG = "CELL";
    @Nonnull
    private final CellCoordinatesFactory cellCoordinatesFactory;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    CellBindParser(CellCoordinatesFactory cellCoordinatesFactory) {
        this.cellCoordinatesFactory = cellCoordinatesFactory;
    }

    /**
     * Parses CellBind from XMLStreamReader. Expects current position in reader is on start element of CellBind,
     * but does not verify this assumption. When finished, reader is positioned on end element of cell bind.
     *
     * CellBind should have one element {@code <COLUMN>} that contains string with column name and one element
     * {@code <CELL>} that contains cell coordinates in Excel format (with relative addresses and without sheet
     * specification)
     *
     * @param reader is XML reader containing cell bind definition
     * @return cell bind object read from stream
     * @throws XMLStreamException when error occurs reading XML stream
     */
    CellBind parse(XMLStreamReader reader) throws XMLStreamException {
        CellBindBuilder builder = new CellBindBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case COLUMN_TAG:
                        String column = reader.getElementText();
                        builder.getSourceColumn().
                                ifPresent((val) -> {throw new RuntimeException("Duplicate " + COLUMN_TAG + " element");});
                        builder.setSourceColumn(column);
                        break;
                    case CELL_TAG:
                        String address = reader.getElementText();
                        builder.getCoordinates().
                                ifPresent((val) -> {throw new RuntimeException("Duplicate " + CELL_TAG + " element");});
                        builder.setCoordinates(cellCoordinatesFactory.parse(address));
                        break;
                    default:
                        throw new RuntimeException("CellBind: Unsupported element " + reader.getLocalName() +
                                " in BIND; supported elements are " + COLUMN_TAG + ", " + CELL_TAG);
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return builder.build();
    }

}
