package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    @Nullable
    private String getAttributeValue(String columnName) {
        Node attrNode = document.getDocumentElement().getAttributes().getNamedItem(columnName.substring(1));
        return (attrNode == null) ? null : attrNode.getNodeValue();
    }

    @Nullable
    private String getElementValue(String columnName) {
        var children = document.getDocumentElement().getChildNodes();
        Element elementNode = null;
        for (int child = 0; child<children.getLength(); child++) {
            if ((children.item(child) instanceof Element) &&
                    ((Element) children.item(child)).getTagName().equals(columnName)) {
                if (elementNode != null) {
                    throw new RuntimeException("Multiple elements " + columnName + " in XML data source row");
                }
                elementNode = (Element) children.item(child);
            }
        }
        return (elementNode == null) ? null : elementNode.getNodeValue();
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        String value = (columnName.charAt(0) == '@') ? getAttributeValue(columnName) : getElementValue(columnName);
        return Optional.ofNullable(value);
    }
}
