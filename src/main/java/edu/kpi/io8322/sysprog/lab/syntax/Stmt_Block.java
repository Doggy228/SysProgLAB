package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Block extends Stmt {
    private Stmt_Seq body;
    private String blockIndent;

    public Stmt_Block(int row, int col, String blockIndent) {
        super(row, col);
        this.blockIndent = blockIndent;
    }

    public NodeType getType(){
        return NodeType.STMT_BLOCK;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]"+System.lineSeparator());
        if(body!=null){
            buf.append(indent+"  {bodyBlock}:"+System.lineSeparator());
            body.printTree(buf, "    "+indent);
        }
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        if(body!=null){
            int labelBlockBegin = prg.newLabel();
            int labelBlockAfter = prg.newLabel();
            prg.outWriteLabel(labelBlockBegin);
            body.gen(prg, labelBlockBegin, labelBlockAfter);
            prg.outWriteLabel(labelBlockAfter);
        }
    }

}
