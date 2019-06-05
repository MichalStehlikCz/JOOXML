package com.provys.report.jooxml.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

/**
 * Parses {@link SelectDataSource} from XML file.
 * Definition is enclosed in SELECTDATASOURCE tag. It supports NAME_NM element for internal name and SELECT element for
 * select statement. All other elements are interpreted as children and passed to {@link ChildDataSourceParser}
 */
class SelectDataSourceParser implements DataSourceParser {

    private static final Logger LOG = LogManager.getLogger(SelectDataSourceParser.class);
    static final String TAG = "SELECTDATASOURCE";
    private static final String NAME_NM_TAG = "NAME_NM";
    private static final String SELECT_TAG = "SELECT";

    @Nonnull
    private final ChildDataSourceParser childDataSourceParser;

    SelectDataSourceParser(ChildDataSourceParser childDataSourceParser) {
        this.childDataSourceParser = Objects.requireNonNull(childDataSourceParser);
    }

    @Override
    @Nonnull
    public SelectDataSourceBuilder parse(@Nullable DataSourceBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        var builder = new SelectDataSourceBuilder().setParent(parent);
        boolean readNameNm = false;
        boolean readSelect = false;
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                switch (name) {
                    case NAME_NM_TAG:
                        if (readNameNm) {
                            throw new RuntimeException("Duplicate tag " + NAME_NM_TAG);
                        }
                        readNameNm = true;
                        builder.setNameNm(reader.getElementText());
                        break;
                    case SELECT_TAG:
                        if (readSelect) {
                            throw new RuntimeException("Duplicate tag " + NAME_NM_TAG);
                        }
                        readSelect = true;
                        builder.setSelectStatement(reader.getElementText());
                        break;
                    default:
                        builder.addChild(childDataSourceParser.parse(builder, reader));
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        LOG.debug("Parsed select datasource {}", () -> builder.getNameNm().orElse(null));
        return builder;
    }

}
