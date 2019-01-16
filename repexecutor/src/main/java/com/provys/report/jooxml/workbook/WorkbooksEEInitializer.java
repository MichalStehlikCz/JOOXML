package com.provys.report.jooxml.workbook;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

/**
 * Class ensures proper Workbooks class initialisation in Java EE / Servlet container.
 * Note that class will not properly load in Java SE environment, as ServletContext is not available and debug messages
 * from bean manager should be ignored
 */
@SuppressWarnings("unused")
@Singleton
class WorkbooksEEInitializer {

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private WorkbookProvider workbookProvider;

    /**
     * Ensures initialisation in Java EE / Servlet container
     * @param payload is ignored
     */
    public void initEE(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
        Workbooks.setWorkbookProvider(workbookProvider);
    }

}
