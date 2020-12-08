package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.Token;

public class Expr_IdVar extends Expr_Id {
    public Expr_IdVar(Token token) throws CompileException {
        super(token);
    }

    public NodeType getType(){
        return NodeType.EXPR_IDVAR;
    }
}
