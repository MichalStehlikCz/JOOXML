package com.provys.report.jooxml.datasource;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

public class XmlChildDataCursor extends StreamDataCursorAncestor<XmlChildDataContext> {

    private final NodeList nodes;
    private int index = 0;

    XmlChildDataCursor(XmlChildDataContext dataContext, Element masterElement) {
        super(dataContext);
        nodes = masterElement.getChildNodes();
    }

    @Nonnull
    @Override
    DataRecord getNext(long rowNumber) {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return new XmlDataRecord(getDataContext().getReportContext(), rowNumber, (Element) nodes.item(index++));
    }

    @Override
    public boolean hasNext() {
        while (index<nodes.getLength()) {
            if ((nodes.item(index) instanceof Element) &&
                    ((Element) nodes.item(index)).getTagName().equals(getDataContext().getDataSource().getRowTag())) {
                return true;
            }
            index++;
        }
        return false;
    }
}
