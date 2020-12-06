package edu.kpi.io8322.sysprog.lab.lexical;

import edu.kpi.io8322.sysprog.lab.PythonCompiler;
import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LexicalAnalyzer {
    private List<String> srclines;
    private List<Token> tokenList;
    private LexFabric lexFabric;

    public LexicalAnalyzer(List<String> srclines) {
        this.srclines = srclines;
        this.lexFabric = new LexFabric(this);
    }

    public void exec() throws CompileException {
        PythonCompiler.app.logInfo(null, null, "Lexical analyzer starting.");
        tokenList = new ArrayList<>();
        for (int row = 0; row < srclines.size(); row++) {
            String line = srclines.get(row);
            int block_indent = 0;
            while (block_indent < line.length() && line.charAt(block_indent) == ' ') {
                block_indent++;
            }
            if (block_indent > line.length()) continue;
            tokenList.add(new Token(lexFabric.getLexType(LexTypeEnum.BLOCKINDENT), row + 1, 1, line.substring(0, block_indent)));
            int col = block_indent;
            while (col < line.length()) {
                if (line.charAt(col) == ' ') {
                    col++;
                    continue;
                }
                LexType lexType = lexFabric.getLexType_symb(line.charAt(col));
                if (lexType != null) {
                    Token token = new Token(lexType, row + 1, col + 1, null);
                    tokenList.add(token);
                    col++;
                    if (lexType.getType() == LexTypeEnum.QUOTE1 || lexType.getType() == LexTypeEnum.QUOTE2) {
                        int pos = -1;
                        switch(lexType.getType()){
                            case QUOTE1:
                                pos = line.indexOf('\'', col);
                                break;
                            case QUOTE2:
                                pos = line.indexOf('\"', col);
                                break;
                        }
                        if(pos<0){
                            TokenInvalid tokenInvalid = new TokenInvalid(lexFabric, row + 1, col + 1, null, "Not a closed quote.");
                            tokenList.add(tokenInvalid);
                            tokenInvalid.throwCompileException();
                        } else if(pos==col+1){
                            tokenList.add(new Token(lexFabric.getLexType(LexTypeEnum.CONSTCHAR), row + 1, col + 1, line.substring(col, pos)));
                        } else {
                            tokenList.add(new Token(lexFabric.getLexType(LexTypeEnum.CONSTSTR), row + 1, col + 1, line.substring(col, pos)));
                        }
                        tokenList.add(new Token(lexType, row+1, pos+1, null));
                        col = pos+1;
                    }
                    continue;
                }
                int next = searchToken(line, col);
                if (col == next) {
                    TokenInvalid tokenInvalid = new TokenInvalid(lexFabric, row + 1, col + 1, line.substring(col, col + 1), "Bad symbol.");
                    tokenList.add(tokenInvalid);
                    tokenInvalid.throwCompileException();
                }
                String tokenValue = line.substring(col, next);
                lexType = lexFabric.getLexType_keyword(tokenValue);
                if (lexType != null) {
                    tokenList.add(new Token(lexType, row + 1, col + 1, null));
                    col = next;
                    continue;
                }
                if (checkConstInt(tokenValue)) {
                    tokenList.add(new Token(lexFabric.getLexType(LexTypeEnum.CONSTINT), row + 1, col + 1, tokenValue));
                    col = next;
                    continue;
                }
                if (checkId(tokenValue)) {
                    tokenList.add(new Token(lexFabric.getLexType(LexTypeEnum.ID), row + 1, col + 1, tokenValue));
                    col = next;
                    continue;
                }
                TokenInvalid tokenInvalid = new TokenInvalid(lexFabric, row + 1, col + 1, tokenValue, "Bad identifier.");
                tokenList.add(tokenInvalid);
                tokenInvalid.throwCompileException();
            }
        }
        PythonCompiler.app.logInfo(null, null, "Lexical analyzer finished OK. [" + tokenList.size() + " lexems]");
    }

    private int searchToken(String line, int start) {
        int next = start;
        while (next < line.length()) {
            if ((line.charAt(next) >= 'A' && line.charAt(next) <= 'Z') ||
                    (line.charAt(next) >= 'a' && line.charAt(next) <= 'z') ||
                    (line.charAt(next) >= '0' && line.charAt(next) <= '9') ||
                    line.charAt(next) == '_') {
                next++;
            } else {
                return next;
            }
        }
        return next;
    }

    private boolean checkConstInt(String value) {
        if (value.length() > 10) return false;
        if (value.startsWith("0x")) {
            if(value.length()==2) return false;
            for (int i = 2; i < value.length(); i++) {
                if (!((value.charAt(i) >= '0' && value.charAt(i) <= '9') ||
                        (value.charAt(i) >= 'a' && value.charAt(i) <= 'f') ||
                        (value.charAt(i) >= 'A' && value.charAt(i) <= 'F'))) return false;
            }
            if (Long.parseLong(value.substring(2), 16) > Integer.MAX_VALUE) return false;
        } else {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) < '0' || value.charAt(i) > '9') return false;
            }
            if (Long.parseLong(value, 10) > Integer.MAX_VALUE) return false;
        }
        return true;
    }

    private boolean checkId(String value) {
        if (!((value.charAt(0) >= 'A' && value.charAt(0) <= 'Z') || (value.charAt(0) >= 'a' && value.charAt(0) <= 'z')))
            return false;
        for (int i = 1; i < value.length(); i++) {
            if (!((value.charAt(i) >= 'A' && value.charAt(i) <= 'Z') ||
                    (value.charAt(i) >= 'a' && value.charAt(i) <= 'z') ||
                    (value.charAt(i) >= '0' && value.charAt(i) <= '9') ||
                    value.charAt(i) == '_'))
                return false;
        }
        return true;
    }

    public void printTokenList(){
        System.out.println("TOKEN LIST:");
        System.out.println("-----------------------------------------------");
        for(Token token: tokenList){
            System.out.println("  "+token.toString());
        }
        System.out.println("-----------------------------------------------");
    }
}
