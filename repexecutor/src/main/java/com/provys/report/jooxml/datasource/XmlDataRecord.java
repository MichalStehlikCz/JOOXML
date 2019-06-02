package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.ReportContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * DataRecord based on element in XmlDom document. Values are retrieved from attributes and child elements of this
 * element. Note that while its fields are immutable, it does not create copy of dom document on its creation and thus
 * risks that document will be modified, effectively modifying results, returned by this DataRecord. It is deliberate
 * decision made for performance reasons, but it is not guaranteed this behaviour will be kept in future and should not
 * be relied on
 */
class XmlDataRecord extends DataRecordRowAncestor {

    @Nonnull
    private final Element element;

    XmlDataRecord(ReportContext reportContext, long rowNumber, Document document) {
        super(reportContext, rowNumber);
        this.element = document.getDocumentElement();
    }

    XmlDataRecord(ReportContext reportContext, long rowNumber, Element element) {
        super(reportContext, rowNumber);
        this.element = Objects.requireNonNull(element);
    }

    @Nonnull
    Element getElement() {
        return element;
    }

    @Nullable
    private String getAttributeValue(String columnName) {
        Node attrNode = element.getAttributes().getNamedItem(columnName.substring(1));
        return (attrNode == null) ? null : attrNode.getNodeValue();
    }

    @Nullable
    private String getElementValue(String columnName) {
        return getChildElement(columnName).map(Element::getTextContent).orElse(null);
    }

    @Nonnull
    @Override
    public Optional<Object> getValue(String columnName, @Nullable Class<?> prefClass) {
        String value = (columnName.charAt(0) == '@') ? getAttributeValue(columnName) : getElementValue(columnName);
        return Optional.ofNullable(value);
    }

    @Nonnull
    Optional<Element> getChildElement(String tagName) {
        var children = element.getChildNodes();
        Element elementNode = null;
        for (int child = 0; child<children.getLength(); child++) {
            if ((children.item(child) instanceof Element) &&
                    ((Element) children.item(child)).getTagName().equals(tagName)) {
                if (elementNode != null) {
                    throw new RuntimeException("Multiple elements " + tagName + " in XML data source row");
                }
                elementNode = (Element) children.item(child);
            }
        }
        return Optional.ofNullable(elementNode);
    }

    @Override
    public String toString() {
        return "XmlDataRecord{" +
                "element=" + element +
                '}';
    }
}
