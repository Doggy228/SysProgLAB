package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.LexTypeEnum;
import edu.kpi.io8322.sysprog.lab.lexical.Token;
import lombok.Getter;

@Getter
public abstract class Expr_Id extends Expr {
    private String name;

    public Expr_Id(Token token) throws CompileException {
        super(token.getRow(), token.getCol());
        if(token.getLexType().getType()!=LexTypeEnum.ID) token.generateCompileException("Token not identifier.");
        this.name = token.getValue();
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: \""+name+"\""+System.lineSeparator());
    }
}
