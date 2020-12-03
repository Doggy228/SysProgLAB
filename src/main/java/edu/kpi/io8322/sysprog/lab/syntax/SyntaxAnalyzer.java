package edu.kpi.io8322.sysprog.lab.syntax;

import edu.kpi.io8322.sysprog.lab.PythonCompiler;
import edu.kpi.io8322.sysprog.lab.core.CompileException;
import edu.kpi.io8322.sysprog.lab.lexical.LexTypeEnum;
import edu.kpi.io8322.sysprog.lab.lexical.Token;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

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

    public Stmt_Program getRoot() {
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
        env = new Env(env, functionCur);
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
        while (tokenPeek(0) != null) {
            parseFunction();
        }
        Stmt_Function func_main = root.findFunction("main");
        if (func_main == null) {
            throw new CompileException(1, 1, "Function main() not defined.", null);
        }
        if (!func_main.getParams().isEmpty()) {
            throw new CompileException(1, 1, "Function main() not allowed params.", null);
        }
        PythonCompiler.app.logInfo(null, null, "Syntax analyzer finished OK.");
    }

    public Stmt_Function parseFunction() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        String blockIndent = tokenCur().getValue();
        tokenNext();
        Token tokenDef = tokenCur();
        if (tokenDef.getLexType().getType() != LexTypeEnum.DEF)
            tokenDef.generateCompileException("Token not keyword \"def\".");
        tokenNext();
        Expr_IdFunction nameFunction = new Expr_IdFunction(tokenCur());
        if (root.findFunction(nameFunction.getName()) != null)
            tokenCur().generateCompileException("Function with name \"" + nameFunction.getName() + "\" already defined.");
        Stmt_Function stmtFunction = new Stmt_Function(tokenDef.getRow(), tokenDef.getCol(), nameFunction, blockIndent);
        root.getFunctionList().add(stmtFunction);
        functionCur = stmtFunction;
        blockStack.clear();
        stmtFunction.setEnv(newEnv());
        tokenNext();
        if (tokenCur().getLexType().getType() != LexTypeEnum.BKTB)
            tokenCur().generateCompileException("Not symbol \"(\".");
        tokenNext();
        parseFunctionParams();
        if (tokenCur().getLexType().getType() != LexTypeEnum.COLON)
            tokenCur().generateCompileException("Not symbol \":\".");
        tokenNext();
        stmtFunction.setBody(parseBlock());
        functionCur = null;
        blockStack.clear();
        restorePrevEnv();
        return stmtFunction;
    }

    public void parseFunctionParams() throws CompileException {
        switch (tokenCur().getLexType().getType()) {
            case BKTE:
                tokenNext();
                return;
            case ID:
                if (functionCur.getEnv().getVar(tokenCur().getValue()) != null)
                    tokenCur().generateCompileException("Dublicate paramname.");
                Expr_IdVarFunc param = new Expr_IdVarFunc(tokenCur());
                functionCur.getParams().add(param);
                functionCur.getEnv().putVar(param);
                tokenNext();
                if (tokenCur().getLexType().getType() == LexTypeEnum.COMMA) {
                    tokenNext();
                    parseFunctionParams();
                    return;
                }
                if (tokenCur().getLexType().getType() == LexTypeEnum.BKTE) {
                    tokenNext();
                    return;
                }
                tokenCur().generateCompileException("Symbol not \",\" or \")\".");
        }
        tokenCur().generateCompileException("Token not paramname or \")\".");
    }

    public Stmt_Block parseBlock() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        Stmt_Block stmt_block = new Stmt_Block(tokenCur().getRow(), tokenCur().getCol(), tokenCur().getValue());
        blockStack.addFirst(stmt_block);
        stmt_block.setBody(parseSeq(stmt_block.getBlockIndent()));
        blockStack.removeFirst();
        return stmt_block;
    }

    public Stmt_Seq parseSeq(String blockIndent) throws CompileException {
        if (tokenPeek(0) == null) return null;
        while (tokenCur().getLexType().getType()==LexTypeEnum.BLOCKINDENT && tokenPeek(1)!=null && tokenPeek(1).getLexType().getType()==LexTypeEnum.BLOCKINDENT){
            tokenNext();;
        }
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKINDENT)
            tokenCur().generateCompileException("Token not block indent.");
        if (tokenPeek(1)==null || !tokenCur().getValue().equals(blockIndent)) return null;
        tokenNext();
        return new Stmt_Seq(tokenCur().getRow(), tokenCur().getCol(), parseStmt(), parseSeq(blockIndent));
    }

    public Stmt parseStmt() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.RETURN) return parseReturn();
        if (tokenCur().getLexType().getType() == LexTypeEnum.ID) return parseIdLeft();
        tokenCur().generateCompileException("Token not statement.");
        return null;
    }

    public Stmt parseReturn() throws CompileException {
        Token tokenReturn = tokenCur();
        if (functionCur == null) tokenReturn.generateCompileException("Return without function.");
        tokenNext();
        return new Stmt_Return(tokenReturn.getRow(), tokenReturn.getCol(), functionCur, parseExpr());
    }

    public Stmt parseIdLeft() throws CompileException {
        Token tokenId = tokenCur();
        if (tokenPeek(1) == null) tokenId.generateCompileException("Not found symbol \"=\".");
        boolean varIsNew = false;
        Expr_IdVar varName = env.getVar(tokenId.getValue());
        if (varName == null) {
            varName = new Expr_IdVar(tokenId);
            env.putVar(varName);
            varIsNew = true;
        }
        tokenNext();
        switch (tokenCur().getLexType().getType()) {
            case EQUAL:
                tokenNext();
                return new Stmt_Set(tokenId.getRow(), tokenId.getCol(), varName, parseExpr());
            case PLUS:
                if (tokenPeek(1) == null || tokenPeek(1).getLexType().getType() != LexTypeEnum.EQUAL) {
                    tokenCur().generateCompileException("Not found \"+=\".");
                }
                if (varIsNew) {
                    tokenId.generateCompileException("Variable \"" + tokenId.getValue() + "\" not found.");
                }
                tokenNext();
                tokenNext();
                Expr_BinaryPlus expr = new Expr_BinaryPlus(tokenCur().getRow(), tokenCur().getCol(), new Expr_GetVarValue(tokenCur().getRow(), tokenCur().getCol(), varName), parseExpr());
                return new Stmt_Set(tokenId.getRow(), tokenId.getCol(), varName, expr);
        }
        tokenCur().generateCompileException("Symbol not \"=\" or \"+=\".");
        return null;
    }

    public Expr parseTerm() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.BKTB) {
            tokenNext();
            Expr expr = parseExpr();
            if (tokenCur().getLexType().getType() != LexTypeEnum.BKTE)
                tokenCur().generateCompileException("Token not \")\".");
            tokenNext();
            return expr;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.QUOTE1 || tokenCur().getLexType().getType() == LexTypeEnum.QUOTE2) {
            Token quoteBegin = tokenCur();
            tokenNext();
            Expr_ConstInt expr = new Expr_ConstInt(tokenCur().getRow(), tokenCur().getCol(), tokenCur());
            tokenNext();
            if (tokenCur().getLexType().getType() != quoteBegin.getLexType().getType())
                tokenCur().generateCompileException("Not close quote.");
            tokenNext();
            return expr;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.CONSTINT) {
            Expr_ConstInt expr = new Expr_ConstInt(tokenCur().getRow(), tokenCur().getCol(), tokenCur());
            tokenNext();
            return expr;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.NOT) {
            Token token_not = tokenCur();
            tokenNext();
            Expr expr = new Expr_UnaryNot(token_not.getRow(), token_not.getCol(), parseExpr());
            return expr;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.ID) {
            if (tokenPeek(1) != null && tokenPeek(1).getLexType().getType() == LexTypeEnum.BKTB) {
                Token tokenFuncName = tokenCur();
                Stmt_Function func = root.findFunction(tokenFuncName.getValue());
                if(func==null) tokenFuncName.generateCompileException("Function \""+tokenFuncName.getValue()+"\" not defined.");
                tokenNext();
                tokenNext();
                Expr_Call expr = new Expr_Call(tokenFuncName.getRow(), tokenFuncName.getCol(), func);
                parseCallParams(expr);
                if(expr.getValues().size()!=func.getParams().size()){
                    tokenFuncName.generateCompileException("Mismatch count function params: "+func.getParams().size()+"<>"+expr.getValues().size());
                }
                return expr;
            } else {
                Expr_IdVar varName = env.getVar(tokenCur().getValue());
                if (varName == null)
                    tokenCur().generateCompileException("Variable \"" + tokenCur().getValue() + "\" not defined.");
                Expr expr = new Expr_GetVarValue(tokenCur().getRow(), tokenCur().getCol(), varName);
                tokenNext();
                return expr;
            }
        }
        tokenCur().generateCompileException("Token not expression.");
        throw new RuntimeException();
    }

    public void parseCallParams(Expr_Call exprCall) throws CompileException {
        if(tokenCur().getLexType().getType()==LexTypeEnum.BKTE) {
            tokenNext();
            return;
        }
        exprCall.getValues().add(parseExpr());
        if(tokenCur().getLexType().getType()==LexTypeEnum.COMMA){
            tokenNext();
            parseCallParams(exprCall);
            return;
        }
        if(tokenCur().getLexType().getType()==LexTypeEnum.BKTE){
            tokenNext();
            return;
        }
        tokenCur().generateCompileException("Symbol not \")\" or \",\".");
    }

    public Expr parseExpr() throws CompileException {
        Expr expr1 = parseExprPrior2();
        if (tokenPeek(0) != null) {
            if (tokenCur().getLexType().getType() == LexTypeEnum.IF) {
                tokenNext();
                Expr cond = parseExprPrior2();
                if (tokenCur().getLexType().getType() != LexTypeEnum.ELSE)
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
        if (tokenPeek(0) != null) {
            if (tokenCur().getLexType().getType() == LexTypeEnum.LESS) {
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
            if (tokenCur().getLexType().getType() == LexTypeEnum.PLUS) {
                tokenNext();
                Expr expr = new Expr_BinaryPlus(expr1.getRow(), expr1.getCol(), expr1, parseExprPrior3());
                return expr;
            }
            if (tokenCur().getLexType().getType() == LexTypeEnum.MINUS) {
                tokenNext();
                Expr expr = new Expr_BinaryMinus(expr1.getRow(), expr1.getCol(), expr1, parseExprPrior3());
                return expr;
            }
        }
        return expr1;
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
        if (num > 0) return "L" + num;
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
