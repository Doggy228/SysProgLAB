package edu.kpi.io8322.sysprog.lab.core;

import edu.kpi.io8322.sysprog.lab.PythonCompiler;
//import edu.kpi.io8322.sysprog.lab.lexical.Token;
import lombok.Getter;

@Getter
public class CompileException extends Exception {
    private int row;
    private int col;

    public CompileException(int row, int col, String message, Throwable cause) {
        super(message, cause);
        this.row = row;
        this.col = col;
    }

/*
    public CompileException(Token token, String message, Throwable cause) {
        super(message, cause);
        this.row = token.getRow();
        this.col = token.getCol();
    }
*/

    @Override
    public String toString() {
        return PythonCompiler.app.getSrcname() +
                "[" + row + "," + col + "]->[" + PythonCompiler.app.getSrclines().get(row - 1) + "]" + System.lineSeparator() + String.format("%1$50s %2$s", "ERR: ", getMessage());
    }
}
