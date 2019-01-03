package com.provys.report.jooxml.workbook;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

@Singleton
public class Workbooks {

    private static Workbooks instance;

    public static CellValueFactory getCellValueFactory() {
        return instance.getCellValueFactory();
    }

    @Inject
    private CellValueFactory cellValueFactory;

    void load(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
        instance = this;
    }
}
