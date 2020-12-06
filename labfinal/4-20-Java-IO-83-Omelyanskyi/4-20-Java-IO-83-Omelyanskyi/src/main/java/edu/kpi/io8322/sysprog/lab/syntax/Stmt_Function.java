package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Function extends Stmt {
    private Expr_IdFunction name;
    private Stmt_Block body;

    public Stmt_Function(int row, int col, Expr_IdFunction name) {
        super(row, col);
        this.name = name;
    }

    public NodeType getType() {
        return NodeType.STMT_FUNCTION;
    }

    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "[" + getType() + "]" + System.lineSeparator());
        buf.append(indent + "  {nameFunction}:" + System.lineSeparator());
        name.printTree(buf, indent+"    ");
        if (body != null) {
            buf.append(indent + "  {bodyFunction}:" + System.lineSeparator());
            body.printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(name.getName()+" PROC");
        if(body!=null){
            body.gen(prg, 0, 0);
        }
        prg.outWriteln(name.getName()+" ENDP");
    }
}