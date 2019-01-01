package com.provys.report.jooxml;

import org.apache.logging.log4j.Level;
import picocli.CommandLine;

public class JooxmlCliApplication {

    public static void main(String args[]){
        CommandLine commandLine = new CommandLine(new RunReport());
        commandLine.registerConverter(Level.class, Level::valueOf);
        commandLine.parseWithHandlers(new CommandLine.RunLast().useOut(System.out)
                , (CommandLine.IExceptionHandler2)(new CommandLine.DefaultExceptionHandler().useErr(System.err)), args);
    }

}
