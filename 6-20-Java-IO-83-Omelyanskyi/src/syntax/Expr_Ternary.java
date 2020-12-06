package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_Ternary extends Expr {
    private Expr ifTrue;
    private Expr cond;
    private Expr ifFalse;

    public Expr_Ternary(int row, int col, Expr ifTrue, Expr cond, Expr ifFalse) {
        super(row, col);
        this.ifTrue = ifTrue;
        this.cond = cond;
        this.ifFalse = ifFalse;
    }

    public NodeType getType() {
        return NodeType.EXPR_TERNARY;
    }

    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "[" + getType() + "]: " + System.lineSeparator());
        buf.append(indent + "  {ifTrue}:" + System.lineSeparator());
        ifTrue.printTree(buf, indent + "    ");
        buf.append(indent + "  {cond}:" + System.lineSeparator());
        cond.printTree(buf, indent + "    ");
        buf.append(indent + "  {ifFalse}:" + System.lineSeparator());
        ifFalse.printTree(buf, indent + "    ");
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        cond.outPushValue(prg);
        prg.outWriteln("\tpop eax");
        int labelFalse = prg.newLabel();
        prg.outWriteln("\tcmp eax,0");
        prg.outWriteln("\tje " + prg.strLabel(labelFalse));
        ifTrue.outPushValue(prg);
        int labelAfter = prg.newLabel();
        prg.outWriteln("\tjmp " + prg.strLabel(labelAfter));
        prg.outWriteLabel(labelFalse);
        ifFalse.outPushValue(prg);
        prg.outWriteLabel(labelAfter);
    }
}
