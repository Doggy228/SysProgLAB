package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.Token;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Expr_IdVar extends Expr_Id {
    private int memOffset;
    public Expr_IdVar(Token token) throws CompileException {
        super(token);
    }

    public NodeType getType(){
        return NodeType.EXPR_IDVAR;
    }

    public void genSave(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpop eax");
        prg.outWriteln("\tmov [ebp-"+memOffset+"],eax");
    }
}
