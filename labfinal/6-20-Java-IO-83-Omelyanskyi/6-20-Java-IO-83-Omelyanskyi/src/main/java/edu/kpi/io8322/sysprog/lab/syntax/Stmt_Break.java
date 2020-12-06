package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Break extends Stmt {
    private Stmt_Cycle cycle;

    public Stmt_Break(int row, int col, Stmt_Cycle cycle) {
        super(row, col);
        this.cycle = cycle;
    }

    public NodeType getType(){
        return NodeType.STMT_BREAK;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: "+System.lineSeparator());
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln("\tjmp " + prg.strLabel(cycle.getLabelBreak()));
    }
}
