package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Block extends Stmt {
    private Stmt body;

    public Stmt_Block(int row, int col) {
        super(row, col);
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
            body.gen(prg, 0, 0);
        }
    }

}
