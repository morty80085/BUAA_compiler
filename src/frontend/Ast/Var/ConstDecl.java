package frontend.Ast.Var;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class ConstDecl extends Node{
    public ConstDecl(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }
}
