package frontend.Ast.Stmt;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class Stmt extends Node{
    public Stmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }
}
