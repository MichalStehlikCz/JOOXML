package com.provys.report.jooxml.report;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

class RowParentAreaParser {

    static final String TAG = "ROWPARENTAREA";

    @Nonnull
    private final StepParser stepParser;

    RowParentAreaParser(StepParser stepParser) {
        this.stepParser = Objects.requireNonNull(stepParser);
    }

    RowParentAreaBuilder parse(@Nullable StepBuilder parent, XMLStreamReader reader) throws XMLStreamException {
        RowParentAreaBuilder builder = new RowParentAreaBuilder(parent);
        stepParser.parseRowChildren(builder, reader);
        return builder;
    }

}
