package frontend.Ast.Exp;


import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class AddExp extends Node{
    public AddExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public Integer getDim() {
        for(Node child : children) {
            if(child.getDim() != null) {
                return child.getDim();
            }
        }
        return null;
    }
}
