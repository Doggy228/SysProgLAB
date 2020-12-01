package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_UnaryNot extends Expr_Unary {
    public Expr_UnaryNot(int row, int col, Expr expr){
        super(row, col, expr);
    }

    public NodeType getType(){
        return NodeType.EXPR_UNARYNOT;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: "+System.lineSeparator());
        getExpr().printTree(buf, indent+"  ");
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        getExpr().outPushValue(prg);
        prg.outWriteln("\tpop eax");
        int labelFalse = prg.newLabel();
        prg.outWriteln("\tcmp eax,0");
        prg.outWriteln("\tje "+prg.strLabel(labelFalse));
        int labelAfter = prg.newLabel();
        prg.outWriteln("\tpush dword ptr 0");
        prg.outWriteln("\tjmp "+prg.strLabel(labelAfter));
        prg.outWriteLabel(labelFalse);
        prg.outWriteln("\tpush dword ptr 1");
        prg.outWriteLabel(labelAfter);
    }

}
