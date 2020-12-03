package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_Set extends Stmt {
    private Expr_IdVar varName;
    private Expr varValue;

    public Stmt_Set(int row, int col, Expr_IdVar varName, Expr varValue) {
        super(row, col);
        this.varName = varName;
        this.varValue = varValue;
    }

    public NodeType getType(){
        return NodeType.STMT_SET;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]"+System.lineSeparator());
        if(varName!=null) {
            buf.append(indent + "  {varName}:" + System.lineSeparator());
            varName.printTree(buf, "    " + indent);
        }
        buf.append(indent + "  {varValue}:" + System.lineSeparator());
        varValue.printTree(buf, "    " + indent);
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        varValue.outPushValue(prg);
        if(varName==null){
            prg.outWriteln("\tpop eax");
        } else {
            varName.genSave(prg);
        }
    }
}
