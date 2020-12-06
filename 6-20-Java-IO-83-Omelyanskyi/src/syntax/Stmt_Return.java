package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_Return extends Stmt {
    private Stmt_Function stmtFunction;
    private Expr retValue;

    public Stmt_Return(int row, int col, Stmt_Function stmtFunction, Expr retValue) {
        super(row, col);
        this.stmtFunction = stmtFunction;
        this.retValue = retValue;
    }

    public NodeType getType(){
        return NodeType.STMT_RETURN;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]"+System.lineSeparator());
        buf.append(indent+"  {retValue}:"+System.lineSeparator());
        retValue.printTree(buf, indent+"    ");
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        retValue.outPushValue(prg);
        prg.outWriteln("\tpop ebx");
        stmtFunction.getEnv().genFreeMem(prg, stmtFunction.getEnv());
        prg.outWriteln("\tret");
    }

}
