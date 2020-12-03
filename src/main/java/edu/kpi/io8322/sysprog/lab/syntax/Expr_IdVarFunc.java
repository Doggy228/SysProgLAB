package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.Token;

import java.io.IOException;

public class Expr_IdVarFunc extends Expr_IdVar {
    public Expr_IdVarFunc(Token token) throws CompileException {
        super(token);
    }

    public NodeType getType(){
        return NodeType.EXPR_IDVARFUNC;
    }

    @Override
    public void genSave(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpop eax");
        prg.outWriteln("\tmov "+getName()+",eax");
    }
}
