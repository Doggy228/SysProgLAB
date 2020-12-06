package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Expr_BinaryLess extends Expr_Binary {
    public Expr_BinaryLess(int row, int col, Expr expr1, Expr expr2){
        super(row, col, expr1, expr2);
    }

    public NodeType getType(){
        return NodeType.EXPR_BINARYLESS;
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpush edx");
        getExpr1().outPushValue(prg);
        getExpr2().outPushValue(prg);
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tpop eax");
        int labelTrue = prg.newLabel();
        prg.outWriteln("\tcmp eax,edx");
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tjl " + prg.strLabel(labelTrue));
        int labelAfter = prg.newLabel();
        prg.outWriteln("\tpush dword ptr 0");
        prg.outWriteln("\tjmp " + prg.strLabel(labelAfter));
        prg.outWriteLabel(labelTrue);
        prg.outWriteln("\tpush dword ptr 1");
        prg.outWriteLabel(labelAfter);
    }
}
