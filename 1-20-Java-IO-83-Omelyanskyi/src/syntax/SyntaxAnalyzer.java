package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.PythonCompiler;
import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.LexTypeEnum;
import edu.kpi.io8322.sysprog.lab.lexical.Token;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntaxAnalyzer {
    private List<Token> tokenList;
    private Stmt_Program root;
    private int tokenIndexCur;
    private Map<Stmt, String> blockIndentMap;
    private Stmt_Function functionCur;
    private int labelIndexCur;
    private Writer out;

    public SyntaxAnalyzer(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Stmt_Program getRoot(){
        return root;
    }

    public Token tokenCur() throws CompileException {
        if (tokenIndexCur < tokenList.size())
            return tokenList.get(tokenIndexCur);
        throw new CompileException(1, 1, "Attempt to parse past end of file.", null);
    }

    public Token tokenPeek(int step) {
        if (tokenIndexCur + step < tokenList.size())
            return tokenList.get(tokenIndexCur + step);
        return null;
    }

    public void tokenNext() {
        tokenIndexCur++;
    }

    public void exec() throws CompileException {
        PythonCompiler.app.logInfo(null, null, "Syntax analyzer starting.");
        tokenIndexCur = 0;
        blockIndentMap = new HashMap<>();
        functionCur = null;
        root = new Stmt_Program();
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        blockIndentMap.put(root, tokenCur().getValue());
        tokenNext();
        root.setStmtFunction(parseFunction());
        PythonCompiler.app.logInfo(null, null, "Syntax analyzer finished OK.");
    }

    public Stmt_Function parseFunction() throws CompileException {
        Token tokenDef = tokenCur();
        if (tokenDef.getLexType().getType() != LexTypeEnum.DEF)
            tokenDef.generateCompileException("Token not keyword \"def\".");
        tokenNext();
        Expr_IdFunction nameFunction = new Expr_IdFunction(tokenCur());
        Stmt_Function stmtFunction = new Stmt_Function(tokenDef.getRow(), tokenDef.getCol(), nameFunction);
        functionCur = stmtFunction;
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.BKTB) tokenCur().generateCompileException("Not symbol \"(\".");
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.BKTE) tokenCur().generateCompileException("Not symbol \")\".");
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.COLON) tokenCur().generateCompileException("Not symbol \":\".");
        tokenNext();
        stmtFunction.setBody(parseBlock());
        functionCur = null;
        return stmtFunction;
    }

    public Stmt_Block parseBlock() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        Stmt_Block stmt_block = new Stmt_Block(tokenCur().getRow(), tokenCur().getCol());
        blockIndentMap.put(stmt_block, tokenCur().getValue());
        tokenNext();
        stmt_block.setBody(parseReturn());
        blockIndentMap.remove(stmt_block);
        return stmt_block;
    }

    public Stmt_Return parseReturn() throws CompileException {
        Token tokenReturn = tokenCur();
        if (tokenReturn.getLexType().getType() != LexTypeEnum.RETURN)
            tokenReturn.generateCompileException("Token not keyword \"return\".");
        if(functionCur==null) tokenReturn.generateCompileException("Return without function.");
        tokenNext();
        return new Stmt_Return(tokenReturn.getRow(),tokenReturn.getCol(), functionCur, parseExpr());
    }

    public Expr parseExpr() throws CompileException {
        Token quoteBegin = null;
        if(tokenCur().getLexType().getType()==LexTypeEnum.QUOTE1 || tokenCur().getLexType().getType()==LexTypeEnum.QUOTE2){
            quoteBegin = tokenCur();
            tokenNext();
        }
        Expr_ConstInt expr = new Expr_ConstInt(tokenCur().getRow(), tokenCur().getCol(), tokenCur());
        tokenNext();
        if(quoteBegin!=null){
            if(tokenCur().getLexType().getType()!=quoteBegin.getLexType().getType()) tokenCur().generateCompileException("Not close quote.");
            tokenNext();
        }
        return expr;
    }

    public void printTree() {
        System.out.println("SYNTAX TREE:");
        System.out.println("-----------------------------------------------");
        StringBuilder bufPrint = new StringBuilder();
        root.printTree(bufPrint, "");
        System.out.println(bufPrint);
        System.out.println("-----------------------------------------------");
    }

    public int newLabel() {
        labelIndexCur++;
        return labelIndexCur;
    }

    public String strLabel(int num) {
        if(num>0) return "L"+num;
        return null;
    }

    public void outWriteLabel(int num) throws IOException {
        if (num > 0)
            out.write("L" + num + ":" + System.lineSeparator());
    }

    public void outWriteln(String str) throws IOException {
        if (str != null && !str.isEmpty()) out.write(str);
        out.write(System.lineSeparator());
    }

    public void execOut(Writer out) throws CompileException, IOException {
        this.out = out;
        labelIndexCur = 0;
        root.gen(this, 0, 0);
    }

}
