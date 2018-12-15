package com.stehlavys.jooxml;

import com.stehlavys.jooxml.repexecutor.RepExecutor;

public class Application {

    public static void main(String args[]){
        if (args.length < 2) {
            throw new IllegalArgumentException("At least 2 parameters required\n\n-template file\n-target file");
        }
        RepExecutor executor = new RepExecutor(args[0], args[1]);
        executor.Run();
        System.out.println("Processing successfully finished");
    }
}
