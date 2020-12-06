package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public abstract class Expr extends Node {
    public Expr(int row, int col){
        super(row, col);
    }

    public String outGetValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        return null;
    }
}
