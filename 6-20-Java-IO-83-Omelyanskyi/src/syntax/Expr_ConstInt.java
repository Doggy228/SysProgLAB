package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.Token;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Expr_ConstInt extends Expr_Const {
    private int value;

    public Expr_ConstInt(int row, int col, Token token) throws CompileException {
        super(row, col);
        switch (token.getLexType().getType()) {
            case CONSTINT:
            case CONSTSTR:
                try {
                    if (token.getValue().startsWith("0x")) {
                        value = Integer.parseInt(token.getValue().substring(2), 16);
                    } else {
                        value = Integer.parseInt(token.getValue(), 10);
                    }
                } catch (Throwable e) {
                    token.generateCompileException("Bad format number");
                }
                break;
            case CONSTCHAR:
                value = token.getValue().charAt(0);
                break;
            default:
                token.generateCompileException("Not integer constant.");
        }
    }

    public NodeType getType(){
        return NodeType.EXPR_CONSTINT;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: \""+value+"\""+System.lineSeparator());
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpush dword ptr "+value);
    }
}
