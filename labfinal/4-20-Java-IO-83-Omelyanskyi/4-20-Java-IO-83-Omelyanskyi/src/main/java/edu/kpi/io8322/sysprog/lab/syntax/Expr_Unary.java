package edu.kpi.io8322.sysprog.lab.syntax;

import lombok.Getter;

@Getter
public abstract class Expr_Unary extends Expr {
    private Expr expr;
    public Expr_Unary(int row, int col, Expr expr){
        super(row, col);
        this.expr = expr;
    }
}
