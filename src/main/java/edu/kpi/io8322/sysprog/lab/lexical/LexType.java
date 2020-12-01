package edu.kpi.io8322.sysprog.lab.lexical;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class LexType {
    private LexicalAnalyzer lexicalAnalyzer;
    public abstract LexTypeEnum getType();
}
