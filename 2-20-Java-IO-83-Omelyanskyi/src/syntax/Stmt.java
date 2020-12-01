package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public abstract class Stmt extends Node {
    public Stmt(int row, int col) {
        super(row, col);
    }

    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
    }
}