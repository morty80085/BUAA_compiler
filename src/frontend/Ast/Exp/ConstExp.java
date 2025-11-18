package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;
//ConstExp → AddExp
public class ConstExp extends Node{
    public ConstExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public int execute() {
        return children.get(0).execute();
    }
}
