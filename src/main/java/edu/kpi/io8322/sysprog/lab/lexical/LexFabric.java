package edu.kpi.io8322.sysprog.lab.lexical;

import java.util.HashMap;
import java.util.Map;

public class LexFabric {
    private Map<LexTypeEnum, LexType> lexTypeMap;
    private Map<Character, LexType_symb> lexTypeSymbMap;
    private Map<String, LexType_keyword> lexTypeKeywordMap;

    public LexFabric(LexicalAnalyzer lexicalAnalyzer) {
        lexTypeMap = new HashMap<>();
        lexTypeMap.put(LexTypeEnum.NONE, new LexType_none());
        lexTypeMap.put(LexTypeEnum.BLOCKINDENT, new LexType_blockindent());
        lexTypeMap.put(LexTypeEnum.BKTB, new LexType_bktb());
        lexTypeMap.put(LexTypeEnum.BKTE, new LexType_bkte());
        lexTypeMap.put(LexTypeEnum.COLON, new LexType_colon());
        lexTypeMap.put(LexTypeEnum.QUOTE1, new LexType_quote1());
        lexTypeMap.put(LexTypeEnum.QUOTE2, new LexType_quote2());
        lexTypeMap.put(LexTypeEnum.DEF, new LexType_def());
        lexTypeMap.put(LexTypeEnum.RETURN, new LexType_return());
        lexTypeMap.put(LexTypeEnum.CONSTINT, new LexType_constint());
        lexTypeMap.put(LexTypeEnum.CONSTCHAR, new LexType_constchar());
        lexTypeMap.put(LexTypeEnum.ID, new LexType_id());
        lexTypeMap.put(LexTypeEnum.NOT, new LexType_not());
        lexTypeMap.put(LexTypeEnum.PLUS, new LexType_plus());
        lexTypeMap.put(LexTypeEnum.MINUS, new LexType_minus());
        lexTypeMap.put(LexTypeEnum.EQUAL, new LexType_equal());
        lexTypeMap.put(LexTypeEnum.LESS, new LexType_less());
        lexTypeMap.put(LexTypeEnum.CONSTSTR, new LexType_conststr());

        lexTypeSymbMap = new HashMap<>();
        lexTypeSymbMap.put(Character.valueOf('('), (LexType_symb) getLexType(LexTypeEnum.BKTB));
        lexTypeSymbMap.put(Character.valueOf(')'), (LexType_symb) getLexType(LexTypeEnum.BKTE));
        lexTypeSymbMap.put(Character.valueOf(':'), (LexType_symb) getLexType(LexTypeEnum.COLON));
        lexTypeSymbMap.put(Character.valueOf('\''), (LexType_symb) getLexType(LexTypeEnum.QUOTE1));
        lexTypeSymbMap.put(Character.valueOf('\"'), (LexType_symb) getLexType(LexTypeEnum.QUOTE2));
        lexTypeSymbMap.put(Character.valueOf('+'), (LexType_symb) getLexType(LexTypeEnum.PLUS));
        lexTypeSymbMap.put(Character.valueOf('-'), (LexType_symb) getLexType(LexTypeEnum.MINUS));
        lexTypeSymbMap.put(Character.valueOf('='), (LexType_symb) getLexType(LexTypeEnum.EQUAL));
        lexTypeSymbMap.put(Character.valueOf('<'), (LexType_symb) getLexType(LexTypeEnum.LESS));

        lexTypeKeywordMap = new HashMap<>();
        lexTypeKeywordMap.put("def", (LexType_keyword) getLexType(LexTypeEnum.DEF));
        lexTypeKeywordMap.put("return", (LexType_keyword) getLexType(LexTypeEnum.RETURN));
        lexTypeKeywordMap.put("not", (LexType_keyword) getLexType(LexTypeEnum.NOT));
    }

    public LexType getLexType(LexTypeEnum type) {
        return lexTypeMap.get(type);
    }

    public LexType_symb getLexType_symb(char value) {
        return lexTypeSymbMap.get(Character.valueOf(value));
    }

    public LexType_keyword getLexType_keyword(String value) {
        return lexTypeKeywordMap.get(value);
    }
}
