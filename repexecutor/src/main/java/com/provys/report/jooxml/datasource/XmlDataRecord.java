package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

class XmlDataRecord extends DataRecordRowAncestor {

    private final Document document;

    XmlDataRecord(ReportContext reportContext, long rowNumber, Document document) {
        super(reportContext, rowNumber);
        this.document = document;
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        String value;
        if (columnName.charAt(0) == '@') {
            // attribute
            Node attrNode = document.getDocumentElement().getAttributes().getNamedItem(columnName.substring(1));
            value = (attrNode == null) ? null : attrNode.getNodeValue();
        } else {
            // element
            NodeList list = document.getElementsByTagName(columnName);
            if (list.getLength() == 1) {
                value = list.item(0).getNodeValue();
            } else if (list.getLength() == 0) {
                value = null;
            } else {
                throw new RuntimeException("Multiple elements " + columnName + " in XML data source row");
            }
        }
        return Optional.ofNullable(value);
    }
}
