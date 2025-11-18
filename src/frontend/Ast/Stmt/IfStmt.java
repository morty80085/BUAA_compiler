package frontend.Ast.Stmt;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class IfStmt extends Node{
    public IfStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }
}
