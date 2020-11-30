package edu.kpi.io8322.sysprog.lab;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PythonCompiler {
    public static PythonCompiler app;

    private String srcname;
    private String dstname;
    private List<String> srclines;

    public PythonCompiler(String srcname) {
        this.srcname = srcname;
        if (srcname.endsWith(".py")) {
            dstname = srcname.substring(0, srcname.length() - ".py".length()) + ".asm";
        } else {
            dstname = srcname + ".asm";
        }
    }

    public int exec() {
        logInfo(null, null, "Source file: " + srcname);
        srclines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(srcname));
            String line = br.readLine();
            while (line != null) {
                srclines.add(line);
                line = br.readLine();
            }
            br.close();
            logInfo(null, null, "Read " + srclines.size() + " rows.");
        } catch (Throwable e) {
            logError(null, null, "Error read file " + srcname);
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable e1) {
                }
            }
            return 1;
        }
        return 0;
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
//            logger.logp(Level.INFO, "Compiler", sourceMethod, msg);
        } else {
//            logger.logp(Level.INFO, sourceClass, sourceMethod, msg);
        }
    }

    public void logError(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
//            logger.logp(Level.SEVERE, "Compiler", sourceMethod, msg);
        } else {
//            logger.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
        }
    }

    public static void main(String[] argc) {
        if (argc.length < 1) {
            System.out.println("Usage: PythonCompiler <source_file>");
            System.out.println();
            System.exit(1);
        }
        PythonCompiler.app = new PythonCompiler(argc[0]);
        long timeexec = System.currentTimeMillis();
        PythonCompiler.app.logInfo(null, null, "Python compiler");
        int exitcode = PythonCompiler.app.exec();
        switch (exitcode) {
            case 0:
                PythonCompiler.app.logInfo(null, null, "Python Compiler finished OK. [ " + (System.currentTimeMillis() - timeexec) + " ms]");
                break;
            default:
                PythonCompiler.app.logError(null, null, "Python Compiler finished ERROR. [ " + (System.currentTimeMillis() - timeexec) + " ms]");
                break;
        }
        System.exit(exitcode);
    }
}


