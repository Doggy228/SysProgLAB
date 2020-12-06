package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public abstract class Expr extends Node {
    public Expr(int row, int col) {
        super(row, col);
    }

    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        throw new CompileException(getRow(), getCol(), "outPushValue not support.", null);
    }
}