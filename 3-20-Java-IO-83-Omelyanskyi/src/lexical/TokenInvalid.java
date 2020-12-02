package edu.kpi.io8322.sysprog.lab.lexical;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.Getter;

@Getter
public class TokenInvalid extends Token {
    private Token real;
    private String errMsg;

    public TokenInvalid(Token real, String errMsg) {
        super(real.getLexType(), real.getRow(), real.getCol(), real.getValue());
        this.real = real;
        this.errMsg = errMsg;
    }

    public TokenInvalid(LexFabric lexFabric, int row, int col, String value, String errMsg) {
        super(lexFabric.getLexType(LexTypeEnum.NONE), row, col, value);
        this.errMsg = errMsg;
    }

    public void throwCompileException() throws CompileException {
        throw new CompileException(this);
    }

    @Override
    public String toString(){
        return "["+getRow()+","+getCol()+"]["+getLexType().getType()+".INVALID]"+(getValue()==null?"":" -> ["+getValue()+"]")+(errMsg==null?"":" // "+errMsg);
    }
}
