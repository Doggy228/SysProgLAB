package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Program extends Stmt {
    private Stmt_Function stmtFunction;

    public Stmt_Program() {
        super(1, 1);
    }

    public NodeType getType(){
        return NodeType.STMT_PROGRAM;
    }

    public void printTree(StringBuilder buf, String indent){
        buf.append(indent+"["+getType()+"]"+System.lineSeparator());
        if(stmtFunction!=null){
            buf.append(indent+"  {function}:"+System.lineSeparator());
            stmtFunction.printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(SyntaxAnalyzer prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(".586");
        prg.outWriteln(".model flat, stdcall");
        prg.outWriteln("option casemap:none");
        prg.outWriteln("include \\masm32\\include\\windows.inc");
        prg.outWriteln("include \\masm32\\include\\kernel32.inc");
        prg.outWriteln("include \\masm32\\include\\masm32.inc");
        prg.outWriteln("includelib \\masm32\\lib\\kernel32.lib");
        prg.outWriteln("includelib \\masm32\\lib\\masm32.lib");
        prg.outWriteln("NumbToStr PROTO :DWORD,:DWORD");
        prg.outWriteln(stmtFunction.getName().getName()+" PROTO");
        prg.outWriteln(".data");
        prg.outWriteln("\tbuff db 11 dup(?)");
        prg.outWriteln(".code");
        prg.outWriteln("start:");
        prg.outWriteln("\tinvoke main");
        prg.outWriteln("\tinvoke NumbToStr, ebx, ADDR buff");
        prg.outWriteln("\tinvoke StdOut,eax");
        prg.outWriteln("\tinvoke ExitProcess,0");
        stmtFunction.gen(prg, 0, 0);
        prg.outWriteln("NumbToStr PROC uses ebx x:DWORD,buffer:DWORD");
        prg.outWriteln("\tmov eax,x");
        prg.outWriteln("\ttest eax,80000000h");
        prg.outWriteln("\tjz LL2");
        prg.outWriteln("\tpush dword ptr 2Dh");
        prg.outWriteln("\tneg eax");
        prg.outWriteln("\tjmp LL3");
        prg.outWriteln("LL2:");
        prg.outWriteln("\tpush dword ptr 20h");
        prg.outWriteln("LL3:");
        prg.outWriteln("\tmov ecx,buffer");
        prg.outWriteln("\tmov ebx,10");
        prg.outWriteln("\tadd ecx,ebx");
        prg.outWriteln("LL1:");
        prg.outWriteln("\txor edx,edx");
        prg.outWriteln("\tdiv ebx");
        prg.outWriteln("\tadd edx,48");
        prg.outWriteln("\tmov BYTE PTR [ecx],dl");
        prg.outWriteln("\tdec ecx");
        prg.outWriteln("\ttest eax,eax");
        prg.outWriteln("\tjnz LL1");
        prg.outWriteln("\tpop edx");
        prg.outWriteln("\tmov BYTE PTR [ecx],dl");
        prg.outWriteln("\tmov eax,ecx");
        prg.outWriteln("\tret");
        prg.outWriteln("NumbToStr ENDP");
        prg.outWriteln("end start");
    }

}
