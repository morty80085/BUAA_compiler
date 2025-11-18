package frontend.Ast.Func;

import frontend.Ast.Exp.Exp;
import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class FuncRParams extends Node{
    public FuncRParams(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public ArrayList<Integer> getFuncRParams() {
        ArrayList<Integer> list = new ArrayList<>();
        for(Node child : children) {
            if(child instanceof Exp) {
                list.add(child.getDim());
            }
        }
        return list;
    }
}
