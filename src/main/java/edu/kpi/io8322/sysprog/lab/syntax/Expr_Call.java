package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Expr_Call extends Expr {
    private Stmt_Function func;
    private List<Expr> values;

    public Expr_Call(int row, int col, Stmt_Function func){
        super(row, col);
        this.func = func;
        values = new ArrayList<>();
    }

    public NodeType getType(){
        return NodeType.EXPR_CALL;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]: "+System.lineSeparator());
        buf.append(indent+"  {funcName}: \""+func.getName().getName()+"\""+System.lineSeparator());
        if(!values.isEmpty()){
            buf.append(indent+"  {values}: "+System.lineSeparator());
            for(Expr value: values){
                value.printTree(buf, indent+"    ");
            }
        }
    }

    @Override
    public void outPushValue(SyntaxAnalyzer prg) throws CompileException, IOException {
        prg.outWriteln("\tpush ebx");
        prg.outWriteln("\tpush ecx");
        prg.outWriteln("\tpush edx");
        for(int i=values.size()-1;i>=0;i--){
            values.get(i).outPushValue(prg);
        }
        prg.outWriteln("\tcall "+func.getName().getName());
        if(!values.isEmpty()){
            prg.outWriteln("\tadd esp,"+(values.size()*4));
        }
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tpop ecx");
        prg.outWriteln("\tpop ebx");
        prg.outWriteln("\tpush eax");;
    }
}
