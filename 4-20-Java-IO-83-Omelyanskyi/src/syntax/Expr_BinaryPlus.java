package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_BinaryPlus extends Expr_Binary {
    public Expr_BinaryPlus(int row, int col, Expr expr1, Expr expr2){
        super(row, col, expr1, expr2);
    }

    public NodeType getType(){
        return NodeType.EXPR_BINARYPLUS;
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpush edx");
        getExpr1().outPushValue(prg);
        getExpr2().outPushValue(prg);
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tpop eax");
        prg.outWriteln("\tadd eax,edx");
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tpush eax");;
    }

}
