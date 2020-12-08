package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.core.CompileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Env {
    private static int SEQ_ID = 1;
    private List<Expr_IdVar> varList;
    protected int memOffsetPos;
    protected Env prev;
    protected Stmt_Function functionCur;
    private int id;
    private int level;

    public Env(Env prev, Stmt_Function functionCur) {
        this.id = SEQ_ID;
        SEQ_ID++;
        memOffsetPos = 0;
        this.prev = prev;
        this.functionCur = functionCur;
        if (prev == null) {
            level = 1;
        } else {
            level = prev.getLevel() + 1;
        }
        varList = new ArrayList<>();
    }

    public Env getPrev() {
        return prev;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int calcSubLevel(Env env) throws CompileException {
        if (env.getId() == id) return 0;
        if (prev == null) throw new CompileException(1, 0, "Error calc sublevel env.", null);
        return prev.calcSubLevel(env) + 1;
    }

    public void putVar(Expr_IdVar node) {
        varList.add(node);
        if(node.getType()!=NodeType.EXPR_IDVARFUNC) {
            memOffsetPos += 4;
            node.setMemOffset(memOffsetPos);
        }
    }

    public Expr_IdVar getVar(String key) {
        for (Expr_IdVar node : varList) {
            if (node.getName().equals(key)) return node;
        }
        if (prev != null) return prev.getVar(key);
        return null;
    }

    public int getMemBlockSize() {
        return memOffsetPos;
    }

    public void genAllocMem(SyntaxAnalyzer prg) throws CompileException, IOException {
        if (getMemBlockSize() > 0) {
            prg.outWriteln("\tpush ebp");
            prg.outWriteln("\tmov ebp,esp");
            prg.outWriteln("\tsub esp," + getMemBlockSize());
        }
    }

    public void genFreeMem(SyntaxAnalyzer prg, Env envCur) throws CompileException, IOException {
        if (getMemBlockSize() > 0) {
            prg.outWriteln("\tmov esp,ebp");
            prg.outWriteln("\tpop ebp");
        }
    }

}
