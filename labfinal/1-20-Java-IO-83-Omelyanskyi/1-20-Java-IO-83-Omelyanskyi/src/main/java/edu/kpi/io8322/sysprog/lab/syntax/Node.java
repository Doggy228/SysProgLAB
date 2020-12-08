package edu.kpi.io8322.sysprog.lab.syntax;

import lombok.Getter;

@Getter
public abstract class Node {
    private int row;
    private int col;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract NodeType getType();

    public abstract void printTree(StringBuilder buf, String indent);
}
