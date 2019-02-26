package com.provys.report.jooxml.tplworkbook;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Interface for factory producing template workbook instances
 */
public interface TplWorkbookFactory {

    /**
     * Create new template workbook based on supplied file
     *
     * @param file is xlsx file used as basis for template workbook
     * @return template workbook making content of given workbook available
     * @throws IOException when there is any problem opening or reading file
     */
    @Nonnull
    TplWorkbook get(File file) throws IOException;
}
