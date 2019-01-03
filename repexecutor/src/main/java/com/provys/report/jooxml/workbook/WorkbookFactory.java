package com.provys.report.jooxml.workbook;

import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

@Singleton
public class WorkbookFactory {

    private static WorkbookFactory instance;

    public static CellValue getFormulaValue(String formula) {
        return instance.getWorkbookFactory().getFormulaValue(formula);
    }

    public static CellValue getStringValue(@Nullable String value) {
        return instance.getWorkbookFactory().getStringValue(value);
    }

    public static CellValue getNumericValue(@Nullable Double value) {
        return instance.getWorkbookFactory().getNumericValue(value);
    }

    public static CellValue getBooleanValue(@Nullable Boolean value) {
        return instance.getWorkbookFactory().getBooleanValue(value);
    }

    public static CellValue getErrorValue(@Nullable Byte value) {
        return instance.getWorkbookFactory().getErrorValue(value);
    }

    public static CellValue getBlankValue() {
        return instance.getWorkbookFactory().getBlankValue();
    }

    public static CellProperties getProperties(@Nullable Integer styleIndex, @Nullable Comment comment) {
        return instance.getWorkbookFactory().getProperties(styleIndex, comment);
    }

    public static Comment getComment(boolean visible, @Nullable String author, @Nullable String text) {
        return instance.getWorkbookFactory().getComment(visible, author, text);
    }

    @Inject
    private WorkbookFactoryInt workbookFactory;

    void load(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
        instance = this;
    }

    WorkbookFactoryInt getWorkbookFactory() {
        return workbookFactory;
    }
}
