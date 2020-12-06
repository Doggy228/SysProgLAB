package edu.kpi.io8322.sysprog.lab.syntax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Stmt_Cycle extends Stmt {
    private Expr cond;
    private Stmt_Block body;
    private int labelBreak;
    private int labelContinue;

    public Stmt_Cycle(int row, int col) {
        super(row, col);
    }


    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: "+System.lineSeparator());
        if(cond!=null){
            buf.append(indent+"  {cond}: "+System.lineSeparator());
            cond.printTree(buf, indent+"    ");
        }
        if(body!=null){
            buf.append(indent+"  {body}: "+System.lineSeparator());
            body.printTree(buf, indent+"    ");
        }
    }
}
