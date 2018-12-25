package com.provys.jooxml.repexecutor;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public interface RepWorkbook extends Closeable {

    void write(OutputStream stream) throws IOException;
}
