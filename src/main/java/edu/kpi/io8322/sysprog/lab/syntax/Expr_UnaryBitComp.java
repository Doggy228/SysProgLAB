package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_UnaryBitComp extends Expr_Unary {
    public Expr_UnaryBitComp(int row, int col, Expr expr){
        super(row, col, expr);
    }

    public NodeType getType(){
        return NodeType.EXPR_UNARYBITCOMP;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: "+System.lineSeparator());
        getExpr().printTree(buf, indent+"  ");
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        getExpr().outPushValue(prg);
        prg.outWriteln("\tpop eax");
        prg.outWriteln("\tnot eax");
        prg.outWriteln("\tpush eax");
    }

}
