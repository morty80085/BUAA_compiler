package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class UnaryOp extends Node{
    public UnaryOp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }
}
