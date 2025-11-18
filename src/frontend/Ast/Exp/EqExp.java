package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class EqExp extends Node{
    public EqExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }
}
