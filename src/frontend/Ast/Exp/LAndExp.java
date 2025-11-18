package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class LAndExp extends Node{
    public LAndExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }
}
