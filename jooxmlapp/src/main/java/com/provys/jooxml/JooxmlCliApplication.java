package com.provys.jooxml;

import com.provys.jooxml.datasource.DataSourceFactory;
import com.provys.jooxml.repexecutor.RepExecutor;
import com.provys.jooxml.repexecutor.Report;
import com.provys.jooxml.repexecutor.ReportDataSource;
import com.provys.jooxml.repexecutor.ReportRegion;
import com.provys.jooxml.report.ReportImpl;
import com.provys.jooxml.report.RowAreaBuilder;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Command(description = "Evaluate JOOXML report", name="jooxml", mixinStandardHelpOptions = true, version = "0.9")
public class JooxmlCliApplication {

    @Option(names = {"--template"}, required = true, description = "Template xlsx file")
    private File template;

    @Option(names = {"-t", "--target"}, required = true, description = "Output file to be generated")
    private File targetFile;

    public static void main(String args[]){
        CommandLine.invoke("run", JooxmlCliApplication.class, args);
    }

    void run() {
        List<ReportDataSource> dataSources = new ArrayList<> (1);
        dataSources.add(DataSourceFactory.getRootDataSource());
        ReportRegion rootRegion = new RowAreaBuilder(dataSources.get(0), 0, Integer.MAX_VALUE);
        Report report = new ReportImpl(dataSources, rootRegion, template);
        RepExecutor executor = new RepExecutor(report, targetFile);
        executor.Run();
        System.out.println("Processing successfully finished");
    }
}
