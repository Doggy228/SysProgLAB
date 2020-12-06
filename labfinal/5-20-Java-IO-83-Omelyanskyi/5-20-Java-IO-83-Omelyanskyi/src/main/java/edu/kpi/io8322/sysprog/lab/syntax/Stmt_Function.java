package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Stmt_Function extends Stmt {
    private Env env;
    private Expr_IdFunction name;
    private List<Expr_IdVarFunc> params;
    private Stmt_Block body;
    private String blockIndent;

    public Stmt_Function(int row, int col, Expr_IdFunction name, String blockIndent) {
        super(row, col);
        this.name = name;
        this.blockIndent = blockIndent;
        params = new ArrayList<>();
    }

    public NodeType getType() {
        return NodeType.STMT_FUNCTION;
    }

    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "[" + getType() + "]" + System.lineSeparator());
        buf.append(indent + "  {nameFunction}:" + System.lineSeparator());
        name.printTree(buf, indent+"    ");
        if(!params.isEmpty()){
            buf.append(indent + "  {params}:" + System.lineSeparator());
            for(Expr_IdVarFunc param: params) {
                param.printTree(buf, indent+"    ");
            }
        }
        if (body != null) {
            buf.append(indent + "  {bodyFunction}:" + System.lineSeparator());
            body.printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        StringBuilder sb = new StringBuilder(name.getName()+" PROC");
        if(!params.isEmpty()){
            sb.append(" p_"+params.get(0).getName()+":DWORD");
            for(int i=1;i<params.size();i++){
                sb.append(",p_"+params.get(i).getName()+":DWORD");
            }
        }
        prg.outWriteln(sb.toString());
        if(body!=null){
            env.genAllocMem(prg);
            body.gen(prg, 0, 0);
            env.genFreeMem(prg, env);
        }
        prg.outWriteln(name.getName()+" ENDP");
    }
}