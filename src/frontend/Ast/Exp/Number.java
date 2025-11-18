package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class Number extends Node{
    public Number(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        return 0;
    }
}
