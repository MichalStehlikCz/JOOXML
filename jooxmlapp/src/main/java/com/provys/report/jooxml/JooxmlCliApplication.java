package com.provys.report.jooxml;

import org.apache.logging.log4j.Level;
import picocli.CommandLine;

public class JooxmlCliApplication {

    @SuppressWarnings("squid:S106") // we do not mind warnings being written to console in CLI application
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new JooxmlInitializer());
        commandLine.registerConverter(Level.class, Level::valueOf);
        //noinspection unchecked
        commandLine.parseWithHandlers(new CommandLine.RunLast().useOut(System.out)
                , (CommandLine.IExceptionHandler2)(new CommandLine.DefaultExceptionHandler().useErr(System.err)), args);
        System.out.println("Processing successfully finished");
    }

}
