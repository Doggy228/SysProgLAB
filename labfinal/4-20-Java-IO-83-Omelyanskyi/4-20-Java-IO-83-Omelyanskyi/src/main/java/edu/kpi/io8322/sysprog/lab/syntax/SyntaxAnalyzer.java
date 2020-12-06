package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.PythonCompiler;
import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.LexTypeEnum;
import edu.kpi.io8322.sysprog.lab.lexical.Token;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class SyntaxAnalyzer {
    private List<Token> tokenList;
    private Stmt_Program root;
    private int tokenIndexCur;
    private Stmt_Function functionCur;
    private Deque<Stmt_Block> blockStack;
    private int labelIndexCur;
    private Writer out;
    private Env env;

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

    public Env newEnv() {
        env = new Env(env);
        return env;
    }

    public Env restorePrevEnv() {
        env = env.getPrev();
        return env;
    }

    public void exec() throws CompileException {
        PythonCompiler.app.logInfo(null, null, "Syntax analyzer starting.");
        tokenIndexCur = 0;
        functionCur = null;
        blockStack = new ArrayDeque<>();
        env = null;
        root = new Stmt_Program();
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        tokenNext();
        root.setStmtFunction(parseFunction());
        if(tokenPeek(0)!=null) tokenCur().generateCompileException("Bad token");
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
        blockStack.clear();
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.BKTB) tokenCur().generateCompileException("Not symbol \"(\".");
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.BKTE) tokenCur().generateCompileException("Not symbol \")\".");
        tokenNext();
        if(tokenCur().getLexType().getType()!=LexTypeEnum.COLON) tokenCur().generateCompileException("Not symbol \":\".");
        tokenNext();
        stmtFunction.setBody(parseBlock());
        functionCur = null;
        blockStack.clear();
        return stmtFunction;
    }

    public Stmt_Block parseBlock() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        Stmt_Block stmt_block = new Stmt_Block(tokenCur().getRow(), tokenCur().getCol(), newEnv(), tokenCur().getValue());
        blockStack.addFirst(stmt_block);
        stmt_block.setBody(parseSeq());
        blockStack.removeFirst();
        return stmt_block;
    }

    public Stmt_Seq parseSeq() throws CompileException {
        if(tokenPeek(0)==null) return null;
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        if(!tokenCur().getValue().equals(blockStack.peekFirst().getBlockIndent())) return null;
        tokenNext();
        return new Stmt_Seq(tokenCur().getRow(), tokenCur().getCol(), parseStmt(),parseSeq());
    }

    public Stmt parseStmt() throws CompileException {
        if(tokenCur().getLexType().getType()==LexTypeEnum.RETURN) return parseReturn();
        if(tokenCur().getLexType().getType()==LexTypeEnum.ID) return parseIdLeft();
        tokenCur().generateCompileException("Token not statement.");
        return null;
    }

    public Stmt parseReturn() throws CompileException {
        Token tokenReturn = tokenCur();
        if(functionCur==null) tokenReturn.generateCompileException("Return without function.");
        tokenNext();
        return new Stmt_Return(tokenReturn.getRow(),tokenReturn.getCol(), functionCur, parseExpr());
    }

    public Stmt parseIdLeft() throws CompileException {
        Token tokenId = tokenCur();
        if(tokenPeek(1)==null || tokenPeek(1).getLexType().getType()!=LexTypeEnum.EQUAL){
            tokenId.generateCompileException("Not found symbol \".");
        }
        Expr_IdVar varName = env.getVar(tokenId.getValue());
        if(varName==null){
            varName = new Expr_IdVar(tokenId);
            env.putVar(varName);
        }
        tokenNext();
        tokenNext();
        return new Stmt_Set(tokenId.getRow(), tokenId.getCol(), varName, parseExpr());
    }

    public Expr parseTerm() throws CompileException {
        if(tokenCur().getLexType().getType()==LexTypeEnum.BKTB){
            tokenNext();
            Expr expr = parseExpr();
            if(tokenCur().getLexType().getType()!=LexTypeEnum.BKTE) tokenCur().generateCompileException("Token not \")\".");
            tokenNext();
            return expr;
        }
        if(tokenCur().getLexType().getType()==LexTypeEnum.QUOTE1 || tokenCur().getLexType().getType()==LexTypeEnum.QUOTE2){
            Token quoteBegin = tokenCur();
            tokenNext();
            Expr_ConstInt expr = new Expr_ConstInt(tokenCur().getRow(), tokenCur().getCol(), tokenCur());
            tokenNext();
            if(tokenCur().getLexType().getType()!=quoteBegin.getLexType().getType()) tokenCur().generateCompileException("Not close quote.");
            tokenNext();
            return expr;
        }
        if(tokenCur().getLexType().getType()==LexTypeEnum.CONSTINT){
            Expr_ConstInt expr = new Expr_ConstInt(tokenCur().getRow(), tokenCur().getCol(), tokenCur());
            tokenNext();
            return expr;
        }
        if(tokenCur().getLexType().getType()==LexTypeEnum.NOT){
            Token token_not = tokenCur();
            tokenNext();
            Expr expr = new Expr_UnaryNot(token_not.getRow(), token_not.getCol(), parseTerm());
            return expr;
        }
        if(tokenCur().getLexType().getType()==LexTypeEnum.ID){
            Expr_IdVar varName = env.getVar(tokenCur().getValue());
            if(varName==null) tokenCur().generateCompileException("Variable \""+tokenCur().getValue()+"\" not defined.");
            Expr expr = new Expr_GetVarValue(tokenCur().getRow(), tokenCur().getCol(), varName);
            tokenNext();
            return expr;
        }
        tokenCur().generateCompileException("Token not expression.");
        throw new RuntimeException();
    }

    public Expr parseExpr() throws CompileException {
        Expr expr1 = parseExprPrior2();
        if(tokenPeek(0)!=null){
            if(tokenCur().getLexType().getType()==LexTypeEnum.IF){
                tokenNext();
                Expr cond = parseExprPrior2();
                if(tokenCur().getLexType().getType()!=LexTypeEnum.ELSE)
                    tokenCur().generateCompileException("Token not \"else\".");
                tokenNext();
                Expr expr = new Expr_Ternary(expr1.getRow(), expr1.getCol(), expr1, cond, parseExpr());
                return expr;
            }
        }
        return expr1;
    }

    public Expr parseExprPrior2() throws CompileException {
        Expr expr1 = parseExprPrior3();
        if(tokenPeek(0)!=null){
            if(tokenCur().getLexType().getType()==LexTypeEnum.LESS){
                tokenNext();
                Expr expr = new Expr_BinaryLess(expr1.getRow(), expr1.getCol(), expr1, parseExprPrior2());
                return expr;
            }
        }
        return expr1;
    }

    public Expr parseExprPrior3() throws CompileException {
        Expr expr1 = parseTerm();
        if (tokenPeek(0) != null) {
            Expr expr = null;
            if (tokenCur().getLexType().getType() == LexTypeEnum.PLUS) {
                tokenNext();
                expr = new Expr_BinaryPlus(expr1.getRow(), expr1.getCol(), expr1, parseTerm());
            } else if (tokenCur().getLexType().getType() == LexTypeEnum.MINUS) {
                tokenNext();
                expr = new Expr_BinaryMinus(expr1.getRow(), expr1.getCol(), expr1, parseTerm());
            }
            if(expr!=null){
                if(tokenPeek(0)==null) return expr;
                switch(tokenCur().getLexType().getType()){
                    case PLUS:
                        tokenNext();
                        return new Expr_BinaryPlus(expr.getRow(), expr.getCol(), expr, parseExprPrior3());
                    case MINUS:
                        tokenNext();
                        return new Expr_BinaryMinus(expr.getRow(), expr.getCol(), expr, parseExprPrior3());
                    default:
                        return expr;
                }
            }
        }
        return expr1;
    }

    public void printTree() {
        System.out.println(PythonCompiler.NAME_PRG+"SYNTAX TREE:");
        System.out.println(PythonCompiler.NAME_PRG+"-----------------------------------------------");
        StringBuilder bufPrint = new StringBuilder();
        root.printTree(bufPrint, PythonCompiler.NAME_PRG+"");
        System.out.println(bufPrint);
        System.out.println(PythonCompiler.NAME_PRG+"-----------------------------------------------");
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
