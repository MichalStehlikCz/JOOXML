package com.provys.report.jooxml.datasource;

import com.provys.report.jooxml.repexecutor.DataCursor;
import com.provys.report.jooxml.repexecutor.ReportContext;

public class XmlChildDataContext extends DataContextAncestor<XmlChildDataSource> {

    XmlChildDataContext(XmlChildDataSource dataSource, ReportContext reportContext) {
        super(dataSource, reportContext);
    }

    @Override
    public DataCursor execute(DataRecord master) {
        if (!(master instanceof XmlDataRecord)) {
            throw new RuntimeException("Cannot evaluate XML child cursor " + getDataSource().getNameNm() +
                    "; master data record not XmlDataRecord");
        }
        return ((XmlDataRecord) master).getChildElement(getDataSource().getMasterTag())
                .map(element -> (DataCursor) new XmlChildDataCursor(this, element))
                .orElse(new EmptyDataCursor(this));
    }
}
