package edu.kpi.io8322.sysprog.lab.lexical;

import edu.kpi.io8322.sysprog.lab.core.CompileException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Token {
    private LexType lexType;
    private int row;
    private int col;
    private String value;

    @Override
    public String toString(){
        return "["+row+","+col+"]["+lexType.getType()+"]"+(value==null?"":" -> ["+value+"]");
    }

    public void generateCompileException(String errMsg) throws CompileException {
        new TokenInvalid(this, errMsg).throwCompileException();
    }
}
