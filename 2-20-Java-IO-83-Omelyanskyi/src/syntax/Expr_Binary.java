package edu.kpi.io8322.sysprog.lab.syntax;

import lombok.Getter;

@Getter
public abstract class Expr_Binary extends Expr {
    private Expr expr1;
    private Expr expr2;
    public Expr_Binary(int row, int col, Expr expr1, Expr expr2){
        super(row, col);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
}
