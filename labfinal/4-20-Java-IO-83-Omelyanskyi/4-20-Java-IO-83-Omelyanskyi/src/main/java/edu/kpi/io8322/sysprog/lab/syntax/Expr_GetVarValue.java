package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_GetVarValue extends Expr {
    private Expr_IdVar varName;

    public Expr_GetVarValue(int row, int col, Expr_IdVar varName) {
        super(row, col);
        this.varName = varName;
    }

    public NodeType getType() {
        return NodeType.EXPR_GETVARVALUE;
    }

    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "[" + getType() + "]: " + System.lineSeparator());
        varName.printTree(buf, indent + "  ");
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tmov eax,[ebp-" + varName.getMemOffset() + "]");
        prg.outWriteln("\tpush eax");
    }

}
