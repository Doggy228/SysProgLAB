package edu.kpi.io8322.sysprog.lab;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.LexicalAnalyzer;
import edu.kpi.io8322.sysprog.lab.syntax.SyntaxAnalyzer;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PythonCompiler {
    public static final String NAME_PRG = "[6-20-Java-IO-83-Omelyanskyi] ";
    private static final String LOG_FORMAT = NAME_PRG+"[%1$-10s] [%2$-7s] %3$s";
    public static PythonCompiler app;

    private String srcname;
    private String dstname;
    private List<String> srclines;
    private LexicalAnalyzer lexicalAnalyzer;
    private SyntaxAnalyzer syntaxAnalyzer;

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
        lexicalAnalyzer = new LexicalAnalyzer(srclines);
        try {
            lexicalAnalyzer.exec();
            lexicalAnalyzer.printTokenList();;
        } catch(CompileException e){
            lexicalAnalyzer.printTokenList();;
            logError("Lexical", null, e.toString());
            return 1;
        }
        syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer.getTokenList());
        try {
            syntaxAnalyzer.exec();
            syntaxAnalyzer.printTree();
        } catch(CompileException e){
            syntaxAnalyzer.printTree();
            logError("Syntax", null, e.toString());
            return 1;
        }
        try {
            logInfo(null, null, "Generate destination files");
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            syntaxAnalyzer.execOut(writer);
            writer.close();
            String bodyResultFile = new String(stringWriter.getBuffer());
            BufferedWriter writerFile = new BufferedWriter(new FileWriter(dstname));
            writerFile.write(bodyResultFile);
            writerFile.close();
        } catch (Throwable e){
            logError("Generator", null, e.toString());
            return 1;
        }
        logInfo(null, null, "Result file: " + dstname);
        return 0;
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            System.out.println(String.format(LOG_FORMAT, "Compiler", "INFO", msg));
        } else {
            System.out.println(String.format(LOG_FORMAT, sourceClass, "INFO", msg));
        }
    }

    public void logError(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            System.out.println(String.format(LOG_FORMAT, "Compiler", "ERROR", msg));
        } else {
            System.out.println(String.format(LOG_FORMAT, sourceClass, "ERROR", msg));
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


