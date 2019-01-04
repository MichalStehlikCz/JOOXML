package com.provys.report.jooxml.workbook;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class ensures proper Workbooks class initialisation in Java SE environment with Weld
 * Note that class will not properly load in Java EE environment, as ContainerInitialized is not available and debug
 * messages from bean manager should be ignored
 */
@Singleton
class WorkbooksSEInitializer {

    @Inject
    Workbooks workbooks;

    /**
     * Ensures initialisation in Java SE environment with Weld
     */
    public void initSE(@Observes ContainerInitialized event) {
        workbooks.init();
    }

}
