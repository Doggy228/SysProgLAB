package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;

public class Stmt_While extends Stmt_Cycle {

    public Stmt_While(int row, int col) {
        super(row, col);
    }

    public NodeType getType(){
        return NodeType.STMT_WHILE;
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        setLabelContinue(prg.newLabel());
        setLabelBreak(prg.newLabel());
        prg.outWriteLabel(getLabelContinue());
        getCond().outPushValue(prg);
        prg.outWriteln("\tpop eax");
        prg.outWriteln("\tcmp eax,0");
        prg.outWriteln("\tje " + prg.strLabel(getLabelBreak()));
        if(getBody()!=null){
            getBody().gen(prg, 0, 0);
        }
        prg.outWriteln("\tjmp " + prg.strLabel(getLabelContinue()));
        prg.outWriteLabel(getLabelBreak());
    }
}
